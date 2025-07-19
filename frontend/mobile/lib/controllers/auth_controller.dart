import 'package:get/get.dart';
// import '../data/models/auth_response.dart';
import '../data/models/login_request.dart';
import '../data/services/api_service.dart';
import '../data/services/storage_service.dart';

class AuthController extends GetxController {
  final _isLoading = false.obs;
  final _isLoggedIn = false.obs;
  final _errorMessage = ''.obs;

  bool get isLoading => _isLoading.value;
  bool get isLoggedIn => _isLoggedIn.value;
  String get errorMessage => _errorMessage.value;

  @override
  void onInit() {
    super.onInit();
    _checkLoginStatus();
  }

  void _checkLoginStatus() {
    final token = StorageService.to.getToken();
    _isLoggedIn.value = token != null && token.isNotEmpty;
  }

  Future<void> login(String username, String password, bool rememberMe) async {
    try {
      _isLoading.value = true;
      _errorMessage.value = '';

      final request = LoginRequest(
        username: username,
        password: password,
        rememberMe: rememberMe,
      );

      final response = await ApiService.to.login(request);

      if (response.success && response.jwt != null) {
        // Save token
        await StorageService.to.saveToken(response.jwt!);
        await StorageService.to.saveRememberMe(rememberMe);

        // Save credentials if remember me is checked
        if (rememberMe) {
          await StorageService.to.saveCredential(username, password);
        }

        _isLoggedIn.value = true;
        Get.offAllNamed('/home');
      } else {
        _errorMessage.value = response.message;
      }
    } catch (e) {
      _errorMessage.value = 'An unexpected error occurred: ${e.toString()}';
    } finally {
      _isLoading.value = false;
    }
  }

  Future<void> logout() async {
    try {
      await StorageService.to.removeToken();
      await StorageService.to.saveRememberMe(false);
      _isLoggedIn.value = false;
      Get.offAllNamed('/login');
    } catch (e) {
      _errorMessage.value = "Logout failed: ${e.toString()}";
    }
  }

  void clearError() {
    _errorMessage.value = '';
  }
}
