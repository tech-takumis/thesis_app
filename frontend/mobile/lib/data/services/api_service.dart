import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:mobile/data/models/registration_request.dart';
import 'package:mobile/data/models/registration_response.dart';
import '../models/auth_response.dart';
import '../models/login_request.dart';

class ApiService {
  // Choose the correct URL based on your testing environment:

  // For Android Emulator:
  static const String baseUrl = 'http://127.0.0.1:8000';

  static Future<AuthResponse> login(LoginRequest request) async {
    try {
      final url = '$baseUrl/mobile/login';

      final response = await http
          .post(
            Uri.parse(url),
            headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json',
            },
            body: jsonEncode(request.toJson()),
          )
          .timeout(
            const Duration(seconds: 15),
            onTimeout: () {
              throw Exception(
                'Connection timeout - server took too long to respond',
              );
            },
          );

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseData = jsonDecode(response.body);
        return AuthResponse.fromJson(responseData);
      } else if (response.statusCode == 401) {
        final Map<String, dynamic> responseData = jsonDecode(response.body);
        return AuthResponse(
          message: responseData['message'] ?? 'Invalid credentials',
          success: false,
        );
      } else {
        return AuthResponse(
          message: 'Server error (${response.statusCode}): ${response.body}',
          success: false,
        );
      }
    } catch (e) {
      return AuthResponse(
        message: 'Network error: ${e.toString()}',
        success: false,
      );
    }
  }

  // static Future<RegistrationResponse> register(
  //   RegistrationRequest request,
  // ) async {
  //   try {
  //     final url = '$baseUrl/api/v1/register/farmers';
  //     final response = await http
  //         .post(
  //           Uri.parse(url),
  //           headers: {
  //             'Content-Type': 'application/json',
  //             'Accept': 'application/json',
  //           },
  //           body: jsonEncode(request.toJson()),
  //         )
  //         .timeout(
  //           const Duration(seconds: 15),
  //           onTimeout: () {
  //             throw Exception('Server timeout - please try again');
  //           },
  //         );

  //     final Map<String, dynamic> responseData = jsonDecode(response.body);

  //     if (response.statusCode == 200 || response.statusCode == 201) {
  //       return RegistrationResponse.fromJson(responseData);
  //     } else if (response.statusCode == 400) {
  //       return RegistrationResponse.fromJson(responseData);
  //     } else {
  //       return RegistrationResponse(
  //         success: false,
  //         error: 'Server error (${response.statusCode})',
  //         message: responseData['message'] ?? 'Registration failed',
  //       );
  //     }
  //   } catch (e) {
  //     return RegistrationResponse(
  //       success: false,
  //       error: 'Network Error',
  //       message: 'An unexpected error occurred. Please try again.',
  //     );
  //   }
  // }
  static Future<RegistrationResponse> register(
    RegistrationRequest request,
  ) async {
    try {
      final url =
          '$baseUrl/api/v1/register/farmers'; // Adjust endpoint as needed

      print('🚀 Attempting registration to: $url');
      print('📤 Request body: ${jsonEncode(request.toJson())}');

      final response = await http
          .post(
            Uri.parse(url),
            headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json',
            },
            body: jsonEncode(request.toJson()),
          )
          .timeout(
            const Duration(seconds: 15),
            onTimeout: () {
              throw Exception('Server timeout - please try again');
            },
          );

      print('📥 Registration response status: ${response.statusCode}');
      print('📥 Registration response body: ${response.body}');

      final Map<String, dynamic> responseData = jsonDecode(response.body);

      if (response.statusCode == 200 || response.statusCode == 201) {
        return RegistrationResponse.fromJson(responseData);
      } else if (response.statusCode == 400) {
        return RegistrationResponse.fromJson(responseData);
      } else {
        return RegistrationResponse(
          success: false,
          error: 'Server error (${response.statusCode})',
          message: responseData['message'] ?? 'Registration failed',
        );
      }
    } catch (e) {
      print('🚨 Registration API Error: $e');
      return RegistrationResponse(
        success: false,
        error: 'Network Error',
        message: 'An unexpected error occurred. Please try again.',
      );
    }
  }
}
