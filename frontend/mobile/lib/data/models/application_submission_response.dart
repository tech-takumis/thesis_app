class ApplicationSubmissionResponse {
  final bool success;
  final String message;
  final String? error;

  ApplicationSubmissionResponse({
    required this.success,
    required this.message,
    this.error,
  });

  factory ApplicationSubmissionResponse.fromJson(Map<String, dynamic> json) {
    return ApplicationSubmissionResponse(
      success: json['success'] ?? false,
      message: json['message'] ?? 'Unknown response',
      error: json['error'],
    );
  }
}
