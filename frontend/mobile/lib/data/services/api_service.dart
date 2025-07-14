import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/auth_response.dart';
import '../models/login_request.dart';

class ApiService {
  // Choose the correct URL based on your testing environment:

  // For Android Emulator:
  static const String baseUrl = 'http://127.0.0.1:8000';

  // For iOS Simulator:
  // static const String baseUrl = 'http://127.0.0.1:8000';

  // For real device (replace with your computer's IP):
  // static const String baseUrl = 'http://192.168.1.XXX:8000';

  static Future<AuthResponse> login(LoginRequest request) async {
    try {
      final url = '$baseUrl/mobile/login';
      // print('Attempting to connect to: $url');
      // print('Request body: ${jsonEncode(request.toJson())}');

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

      // print('Response status: ${response.statusCode}');
      // print('Response headers: ${response.headers}');
      // print('Response body: ${response.body}');

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
      print('API Error Details: $e');

      // if (e.toString().contains('Connection refused') ||
      //     e.toString().contains('SocketException')) {
      //   return AuthResponse(
      //     message:
      //         'Cannot connect to server at $baseUrl. Please check:\n• Server is running\n• Correct IP address\n• Network connection',
      //     success: false,
      //   );
      // } else if (e.toString().contains('timeout')) {
      //   return AuthResponse(
      //     message: 'Server is taking too long to respond. Please try again.',
      //     success: false,
      //   );
      // } else if (e.toString().contains('FormatException')) {
      //   return AuthResponse(
      //     message: 'Invalid response from server. Please check server logs.',
      //     success: false,
      //   );
      // } else {
      return AuthResponse(
        message: 'Network error: ${e.toString()}',
        success: false,
      );
      // }
    }
  }
}
