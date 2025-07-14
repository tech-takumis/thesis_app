import 'package:shared_preferences/shared_preferences.dart';

class StorageService {
  static SharedPreferences? _prefs;

  static Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  static Future<void> saveToken(String token) async {
    await _prefs?.setString('auth_token', token);
  }

  static Future<String?> getToken() async {
    return _prefs?.getString('auth_token');
  }

  static Future<void> removeToken() async {
    await _prefs?.remove('auth_token');
  }

  static Future<void> saveRememberMe(bool remember) async {
    await _prefs?.setBool('remember_me', remember);
  }

  static Future<bool> getRememberMe() async {
    return _prefs?.getBool('remember_me') ?? false;
  }

  static Future<void> saveCredentials(String username, String password) async {
    await _prefs?.setString('saved_username', username);
    await _prefs?.setString('saved_password', password);
  }

  static Future<Map<String, String?>> getCredentials() async {
    return {
      'username': _prefs?.getString('saved_username'),
      'password': _prefs?.getString('saved_password'),
    };
  }

  static Future<void> clearCredentials() async {
    await _prefs?.remove('saved_username');
    await _prefs?.remove('saved_password');
  }
}
