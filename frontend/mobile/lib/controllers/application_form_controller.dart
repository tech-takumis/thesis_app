import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:geolocator/geolocator.dart'; // Import geolocator
import 'package:rflutter_alert/rflutter_alert.dart';
import '../data/models/application_data.dart';
import '../data/services/api_service.dart';
import '../data/services/location_service.dart'; // Import LocationService

class ApplicationFormController extends GetxController {
  final ApplicationContent application;
  final GlobalKey<FormState> formKey = GlobalKey<FormState>();

  final _isLoading = false.obs;
  final _errorMessage = ''.obs;
  final _successMessage = ''.obs;

  bool get isLoading => _isLoading.value;
  String get errorMessage => _errorMessage.value;
  String get successMessage => _successMessage.value;

  // Dynamic controllers for form fields
  final Map<String, TextEditingController> _textControllers = {};
  final Map<String, Rx<XFile?>> _fileSelections = {};
  final Map<String, GlobalKey<FormFieldState>> _formFieldKeys = {};

  // Coordinate field, if applicable
  final TextEditingController _coordinateController = TextEditingController();
  final GlobalKey<FormFieldState> _coordinateFieldKey =
      GlobalKey<FormFieldState>();
  final bool _hasCoordinateField;

  ApplicationFormController(this.application)
    : _hasCoordinateField = application.fields.any(
        (field) => field.hasCoordinate,
      ) {
    _initializeControllers();
  }

  @override
  void onInit() {
    super.onInit();
    clearMessages();
  }

  void _initializeControllers() {
    for (var field in application.fields) {
      _formFieldKeys[field.key] = GlobalKey<FormFieldState>();
      if (field.fieldType == 'Text' || field.fieldType == 'Number') {
        _textControllers[field.key] = TextEditingController();
      } else if (field.fieldType == 'File') {
        _fileSelections[field.key] = Rx<XFile?>(null);
      }
    }
    if (_hasCoordinateField) {
      _formFieldKeys['coordinate'] = _coordinateFieldKey;
    }
  }

  TextEditingController? getTextFieldController(String key) =>
      _textControllers[key];
  Rx<XFile?>? getFileSelection(String key) => _fileSelections[key];
  GlobalKey<FormFieldState>? getFormFieldKey(String key) => _formFieldKeys[key];
  TextEditingController get coordinateController => _coordinateController;
  GlobalKey<FormFieldState> get coordinateFieldKey => _coordinateFieldKey;
  bool get hasCoordinateField => _hasCoordinateField;

  Future<void> pickFile(String fieldKey) async {
    final ImagePicker picker = ImagePicker();
    final XFile? pickedFile = await picker.pickImage(
      source: ImageSource.camera,
    ); // Always use camera

    if (pickedFile != null) {
      _fileSelections[fieldKey]?.value = pickedFile;
      getFormFieldKey(
        fieldKey,
      )?.currentState?.validate(); // Re-validate the field

      // Get GPS coordinates using geolocator if the application has a coordinate field
      if (_hasCoordinateField) {
        try {
          // Check if location services are enabled and permissions granted
          bool locationReady =
              await LocationService.to.checkAndRequestLocationReadiness();

          if (locationReady) {
            Get.snackbar(
              'Getting Location',
              'Fetching current GPS coordinates...',
              snackPosition: SnackPosition.BOTTOM,
              backgroundColor: Colors.blue.withOpacity(0.8),
              colorText: Colors.white,
              duration: const Duration(seconds: 2),
            );
            Position position = await Geolocator.getCurrentPosition(
              locationSettings: LocationSettings(
                accuracy: LocationAccuracy.high,
                distanceFilter: 0,
                timeLimit: const Duration(seconds: 10),
              ),
            );
            final gpsCoordinate =
                '${position.latitude.toStringAsFixed(6)},${position.longitude.toStringAsFixed(6)}';
            _coordinateController.text = gpsCoordinate;
            _coordinateFieldKey.currentState
                ?.validate(); // Validate coordinate field
            Get.snackbar(
              'GPS Coordinates Captured',
              'Coordinates: $gpsCoordinate',
              snackPosition: SnackPosition.BOTTOM,
              backgroundColor: Colors.green.withOpacity(0.8),
              colorText: Colors.white,
            );
          } else {
            _coordinateController.clear(); // Clear if location not ready
            _coordinateFieldKey.currentState?.validate();
            Get.snackbar(
              'Location Not Available',
              'Please enable location services and grant permissions to capture GPS coordinates.',
              snackPosition: SnackPosition.BOTTOM,
              backgroundColor: Colors.orange.withOpacity(0.8),
              colorText: Colors.white,
              mainButton: TextButton(
                onPressed: () {
                  Get.back(); // Dismiss snackbar
                  LocationService.to.openAppSettings(); // Open app settings
                },
                child: const Text(
                  'Open Settings',
                  style: TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            );
          }
        } catch (e) {
          print('Error getting GPS data: $e');
          _coordinateController.clear(); // Clear on error
          _coordinateFieldKey.currentState?.validate();
          Get.snackbar(
            'Location Error',
            'Could not get GPS coordinates: ${e.toString()}',
            snackPosition: SnackPosition.BOTTOM,
            backgroundColor: Colors.red.withOpacity(0.8),
            colorText: Colors.white,
          );
        }
      }
    }
  }

  void removeFile(String fieldKey) {
    _fileSelections[fieldKey]?.value = null;
    getFormFieldKey(
      fieldKey,
    )?.currentState?.validate(); // Re-validate the field
    // Optionally clear coordinate if the removed file was the source
    if (_hasCoordinateField) {
      _coordinateController.clear();
      _coordinateFieldKey.currentState?.validate();
    }
  }

  String? validateField(String? value, bool required) {
    if (required && (value == null || value.isEmpty)) {
      return 'This field is required';
    }
    return null;
  }

  String? validateFileField(XFile? file, bool required) {
    if (required && file == null) {
      return 'This file is required';
    }
    return null;
  }

  Future<void> submitForm() async {
    if (!formKey.currentState!.validate()) {
      _errorMessage.value =
          'Please fill in all required fields and select all required files.';
      return;
    }

    try {
      _isLoading.value = true;
      _errorMessage.value = '';
      _successMessage.value = '';

      final Map<String, dynamic> fieldValues =
          {}; // This will now contain filenames for file fields
      final Map<String, XFile> filesToUpload =
          {}; // This will contain the actual XFile objects

      // Collect all field values, including filenames for file fields
      for (var field in application.fields) {
        if (field.fieldType == 'Text' || field.fieldType == 'Number') {
          fieldValues[field.key] =
              _textControllers[field.key]?.text.trim() ?? '';
        } else if (field.fieldType == 'File') {
          final file = _fileSelections[field.key]?.value;
          if (file != null) {
            fieldValues[field.key] =
                file.name; // Add filename to fieldValues JSON
            filesToUpload[field.key] = file; // Add actual file to filesToUpload
          } else {
            fieldValues[field.key] = '';
          }
        }
      }

      // Add coordinate field if present and has a value
      if (_hasCoordinateField && _coordinateController.text.isNotEmpty) {
        fieldValues['coordinate'] = _coordinateController.text.trim();
      } else if (_hasCoordinateField) {
        fieldValues['coordinate'] = '';
      }

      final response = await ApiService.to.submitApplicationForm(
        application.id, // Pass the application ID
        fieldValues, // This now includes filenames for file fields
        filesToUpload,
      );

      if (response.success) {
        _successMessage.value = response.message; // Set message for the alert
        _clearForm(); // Clear form fields after successful submission
        _showSuccessAlert(); // Show the success alert
      } else {
        _errorMessage.value = response.message;
      }
    } catch (e) {
      _errorMessage.value = 'An unexpected error occurred: ${e.toString()}';
    } finally {
      _isLoading.value = false;
    }
  }

  void _showSuccessAlert() {
    Alert(
      context: Get.context!, // Use Get.context for the alert context
      type: AlertType.success,
      title: "Application Submitted",
      desc: _successMessage.value, // Use the message from the controller
      buttons: [
        DialogButton(
          onPressed: () {
            Get.back(); // Close the alert dialog
            Get.back(); // Go back to the applications list page
          },
          width: 120,
          child: const Text(
            "OK",
            style: TextStyle(color: Colors.white, fontSize: 20),
          ),
        ),
      ],
    ).show();
  }

  void _clearForm() {
    for (var controller in _textControllers.values) {
      controller.clear();
    }
    for (var rxFile in _fileSelections.values) {
      rxFile.value = null;
    }
    _coordinateController.clear();
    clearMessages();
  }

  void clearMessages() {
    _errorMessage.value = '';
    _successMessage.value = '';
  }

  @override
  void onClose() {
    for (var controller in _textControllers.values) {
      controller.dispose();
    }
    _coordinateController.dispose();
    super.onClose();
  }
}
