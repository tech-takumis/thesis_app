import 'package:http/http.dart' as http;
import 'dart:convert';

class ServerTest {
  static Future<void> testServerEndpoints() async {
    final baseUrls = [
      'http://10.0.2.2:8000', // Android Emulator
      'http://127.0.0.1:8000', // iOS Simulator
    ];

    for (String baseUrl in baseUrls) {
      print('\n=== Testing $baseUrl ===');

      // Test if server is reachable
      try {
        final response = await http
            .get(Uri.parse(baseUrl))
            .timeout(const Duration(seconds: 5));
        print('✅ Server reachable: ${response.statusCode}');
      } catch (e) {
        print('❌ Server not reachable: $e');
        continue;
      }

      // Test the login endpoint
      try {
        final loginResponse = await http
            .post(
              Uri.parse('$baseUrl/mobile/login'),
              headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
              },
              body: jsonEncode({'username': 'test', 'password': 'test'}),
            )
            .timeout(const Duration(seconds: 10));

        print('✅ Login endpoint reachable: ${loginResponse.statusCode}');
        print('Response: ${loginResponse.body}');
      } catch (e) {
        print('❌ Login endpoint failed: $e');
      }
    }
  }
}
