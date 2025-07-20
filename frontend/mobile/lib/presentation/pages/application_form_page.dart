import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../controllers/application_form_controller.dart';
import '../../data/models/application_data.dart';
import '../widgets/custom_text_field.dart';
import '../widgets/custom_button.dart';

class ApplicationFormPage extends StatelessWidget {
  final ApplicationContent application;

  const ApplicationFormPage({super.key, required this.application});

  @override
  Widget build(BuildContext context) {
    // Initialize the controller with the application data
    final ApplicationFormController controller = Get.put(
      ApplicationFormController(application),
    );

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: AppBar(
        title: Text(application.displayName),
        backgroundColor: Theme.of(context).primaryColor,
        foregroundColor: Colors.white,
        elevation: 0,
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(24.0),
          child: Form(
            key: controller.formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Text(
                  application.description,
                  style: Theme.of(
                    context,
                  ).textTheme.titleMedium?.copyWith(color: Colors.grey[700]),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 32),

                // Dynamic fields based on application.fields
                ...application.fields.map((field) {
                  if (field.fieldType == 'Text' ||
                      field.fieldType == 'Number') {
                    return Padding(
                      padding: const EdgeInsets.only(bottom: 16.0),
                      child: CustomTextField(
                        key: controller.getFormFieldKey(field.key),
                        controller:
                            controller.getTextFieldController(field.key)!,
                        label: field.displayName,
                        prefixIcon: Icons.edit_note,
                        keyboardType:
                            field.fieldType == 'Number'
                                ? TextInputType.number
                                : TextInputType.text,
                        validator:
                            (value) =>
                                controller.validateField(value, field.required),
                      ),
                    );
                  } else if (field.fieldType == 'File') {
                    return Padding(
                      padding: const EdgeInsets.only(bottom: 16.0),
                      child: Obx(() {
                        final pickedFile =
                            controller.getFileSelection(field.key)?.value;
                        return Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              field.displayName,
                              style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.w500,
                                color: Colors.grey[800],
                              ),
                            ),
                            Text(
                              field.note,
                              style: TextStyle(
                                fontSize: 12,
                                color: Colors.grey[600],
                              ),
                            ),
                            const SizedBox(height: 8),
                            Container(
                              decoration: BoxDecoration(
                                border: Border.all(color: Colors.grey[300]!),
                                borderRadius: BorderRadius.circular(12),
                                color: Colors.white,
                              ),
                              child: Column(
                                children: [
                                  ListTile(
                                    leading: Icon(
                                      pickedFile != null
                                          ? Icons.check_circle
                                          : Icons
                                              .camera_alt, // Changed icon to camera
                                      color:
                                          pickedFile != null
                                              ? Colors.green
                                              : Theme.of(context).primaryColor,
                                    ),
                                    title: Text(
                                      pickedFile != null
                                          ? pickedFile.name
                                          : 'Tap to take photo', // Changed text
                                      style: TextStyle(
                                        color:
                                            pickedFile != null
                                                ? Colors.green[700]
                                                : Colors.grey[700],
                                      ),
                                    ),
                                    trailing:
                                        pickedFile != null
                                            ? IconButton(
                                              icon: const Icon(
                                                Icons.close,
                                                color: Colors.red,
                                              ),
                                              onPressed:
                                                  () => controller.removeFile(
                                                    field.key,
                                                  ),
                                            )
                                            : null,
                                    onTap: () {
                                      // Directly call pickFile, it's now camera-only
                                      controller.pickFile(field.key);
                                    },
                                  ),
                                  // Manual validation message display for file fields
                                  if (controller
                                          .getFormFieldKey(field.key)
                                          ?.currentState
                                          ?.hasError ==
                                      true)
                                    Padding(
                                      padding: const EdgeInsets.only(
                                        left: 16.0,
                                        right: 16.0,
                                        bottom: 8.0,
                                      ),
                                      child: Align(
                                        alignment: Alignment.centerLeft,
                                        child: Text(
                                          controller
                                                  .getFormFieldKey(field.key)
                                                  ?.currentState
                                                  ?.errorText ??
                                              '',
                                          style: TextStyle(
                                            color:
                                                Theme.of(
                                                  context,
                                                ).colorScheme.error,
                                            fontSize: 12,
                                          ),
                                        ),
                                      ),
                                    ),
                                ],
                              ),
                            ),
                          ],
                        );
                      }),
                    );
                  }
                  return const SizedBox.shrink(); // Fallback for unsupported field types
                }),

                // Coordinate field (if any field requires it)
                if (controller.hasCoordinateField)
                  Padding(
                    padding: const EdgeInsets.only(bottom: 16.0),
                    child: CustomTextField(
                      key: controller.coordinateFieldKey,
                      controller: controller.coordinateController,
                      label: 'Coordinate (Auto-filled from image EXIF)',
                      prefixIcon: Icons.location_on_outlined,
                      keyboardType: TextInputType.text,
                      readOnly: true, // Corrected: Pass readOnly parameter
                      validator:
                          (value) => controller.validateField(
                            value,
                            true,
                          ), // Assuming coordinate is always required if present
                    ),
                  ),

                const SizedBox(height: 24),

                // Removed the success message Obx here as it's handled by rflutter_alert
                // Obx(() {
                //   if (controller.successMessage.isEmpty) return const SizedBox.shrink();
                //   return Container(
                //     padding: const EdgeInsets.all(16),
                //     margin: const EdgeInsets.only(bottom: 16),
                //     decoration: BoxDecoration(
                //       color: Colors.green[50],
                //       border: Border.all(color: Colors.green[300]!),
                //       borderRadius: BorderRadius.circular(12),
                //     ),
                //     child: Row(
                //       children: [
                //         Icon(Icons.check_circle_outline, color: Colors.green[700], size: 24),
                //         const SizedBox(width: 12),
                //         Expanded(
                //           child: Text(
                //             controller.successMessage,
                //             style: TextStyle(
                //               color: Colors.green[700],
                //               fontWeight: FontWeight.w600,
                //               fontSize: 14,
                //             ),
                //           ),
                //         ),
                //         IconButton(
                //           onPressed: controller.clearMessages,
                //           icon: Icon(Icons.close, color: Colors.green[700], size: 16),
                //         ),
                //       ],
                //     ),
                //   );
                // }),

                // Error Message (kept this one)
                Obx(() {
                  if (controller.errorMessage.isEmpty)
                    return const SizedBox.shrink();
                  return Container(
                    padding: const EdgeInsets.all(16),
                    margin: const EdgeInsets.only(bottom: 16),
                    decoration: BoxDecoration(
                      color: Colors.red[50],
                      border: Border.all(color: Colors.red[300]!),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Row(
                      children: [
                        Icon(
                          Icons.error_outline,
                          color: Colors.red[700],
                          size: 24,
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: Text(
                            controller.errorMessage,
                            style: TextStyle(
                              color: Colors.red[700],
                              fontSize: 14,
                            ),
                          ),
                        ),
                        IconButton(
                          onPressed: controller.clearMessages,
                          icon: Icon(
                            Icons.close,
                            color: Colors.red[700],
                            size: 16,
                          ),
                        ),
                      ],
                    ),
                  );
                }),

                // Submit Button
                Obx(
                  () => CustomButton(
                    onPressed:
                        controller.isLoading ? null : controller.submitForm,
                    isLoading: controller.isLoading,
                    child: const Text(
                      'Submit Application',
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 24),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
