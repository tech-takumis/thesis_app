class AuthResponse {
  final String? jwt;
  final String message;
  final bool success;

  AuthResponse({this.jwt, required this.message, required this.success});

  factory AuthResponse.fromJson(Map<String, dynamic> json) {
    return AuthResponse(
      jwt: json['jwt'],
      message: json['message'] ?? '',
      success: json['jwt'] != null,
    );
  }

  Map<String, dynamic> toJson() {
    return {'jwt': jwt, 'message': message, 'success': success};
  }
}
