import '../data/models/auth_response.dart';
import '../data/models/login_request.dart';
import '../data/services/api_service.dart';
import '../data/services/storage_service.dart';

class AuthManager {
  static Future<AuthResponse> login(
    String username,
    String password,
    bool rememberMe,
  ) async {
    final request = LoginRequest(
      username: username,
      password: password,
      rememberMe: rememberMe,
    );
    final response = await ApiService.login(request);

    if (response.success && response.jwt != null) {
      await StorageService.saveToken(response.jwt!);
      await StorageService.saveRememberMe(rememberMe);

      if (rememberMe) {
        await StorageService.saveCredentials(username, password);
      } else {
        await StorageService.clearCredentials();
      }
    }

    return response;
  }

  static Future<void> logout() async {
    await StorageService.removeToken();
    await StorageService.clearCredentials();
    await StorageService.saveRememberMe(false);
  }

  static Future<bool> isLoggedIn() async {
    final token = await StorageService.getToken();
    return token != null && token.isNotEmpty;
  }

  static Future<Map<String, String?>> getSavedCredentials() async {
    final rememberMe = await StorageService.getRememberMe();
    if (rememberMe) {
      return await StorageService.getCredentials();
    }
    return {'username': null, 'password': null};
  }
}
