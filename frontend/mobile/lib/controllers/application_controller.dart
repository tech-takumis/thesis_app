import 'package:get/get.dart';
import '../data/models/application_data.dart';
import '../data/services/api_service.dart';

class ApplicationController extends GetxController {
  final _applications = <ApplicationContent>[].obs;
  final _isLoading = true.obs;
  final _errorMessage = ''.obs;

  List<ApplicationContent> get applications => _applications;
  bool get isLoading => _isLoading.value;
  String get errorMessage => _errorMessage.value;

  @override
  void onInit() {
    super.onInit();
    fetchApplications();
  }

  Future<void> fetchApplications() async {
    try {
      _isLoading.value = true;
      _errorMessage.value = '';
      final response = await ApiService.to.fetchApplications();
      _applications.value = response.content;
    } catch (e) {
      _errorMessage.value = e.toString().replaceFirst('Exception: ', '');
    } finally {
      _isLoading.value = false;
    }
  }

  void clearError() {
    _errorMessage.value = '';
  }
}
