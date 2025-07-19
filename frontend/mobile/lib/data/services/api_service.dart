import 'package:dio/dio.dart';
import 'package:get/get.dart' as getx;
import '../models/auth_response.dart';
import '../models/login_request.dart';
import '../models/registration_request.dart';
import '../models/registration_response.dart';
import '../models/application_data.dart';
import 'storage_service.dart'; // Import StorageService

class ApiService extends getx.GetxService {
  late Dio _dio;
  static const String baseUrl = 'http://localhost:8000/api/v1';

  static ApiService get to => getx.Get.find();

  @override
  void onInit() {
    super.onInit();
    _initializeDio();
  }

  void _initializeDio() {
    _dio = Dio(
      BaseOptions(
        baseUrl: baseUrl,
        connectTimeout: const Duration(seconds: 15),
        receiveTimeout: const Duration(seconds: 15),
        sendTimeout: const Duration(seconds: 15),
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
      ),
    );

    // Add interceptors for logging and authentication
    _dio.interceptors.add(
      LogInterceptor(
        requestBody: true,
        responseBody: true,
        requestHeader: true,
        responseHeader: false,
        error: true,
      ),
    );

    _dio.interceptors.add(
      InterceptorsWrapper(
        onRequest: (options, handler) async {
          // Add JWT token to requests if available
          final token = StorageService.to.getToken();
          if (token != null && token.isNotEmpty) {
            options.headers['Authorization'] = 'Bearer $token';
          }
          return handler.next(options);
        },
        onError: (error, handler) {
          handler.next(error);
        },
      ),
    );
  }

  Future<AuthResponse> login(LoginRequest request) async {
    try {
      final response = await _dio.post('/mobile/login', data: request.toJson());

      return AuthResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (e.type == DioExceptionType.connectionTimeout ||
          e.type == DioExceptionType.receiveTimeout ||
          e.type == DioExceptionType.sendTimeout) {
        return AuthResponse(
          message: 'Connection timeout. Please check your network connection.',
          success: false,
        );
      } else if (e.type == DioExceptionType.connectionError) {
        return AuthResponse(
          message:
              'Cannot connect to server. Please ensure your backend is running on $baseUrl.',
          success: false,
        );
      } else if (e.response?.statusCode == 401 ||
          e.response?.statusCode == 400) {
        final errorData = e.response?.data;
        return AuthResponse(
          message: errorData['message'] ?? 'Invalid credentials',
          success: false,
        );
      } else {
        return AuthResponse(
          message:
              'Server error (${e.response?.statusCode}): ${e.response?.data}',
          success: false,
        );
      }
    } catch (e) {
      return AuthResponse(
        message: 'An unexpected error occurred: ${e.toString()}',
        success: false,
      );
    }
  }

  Future<RegistrationResponse> register(RegistrationRequest request) async {
    try {
      final response = await _dio.post('/farmer', data: request.toJson());

      return RegistrationResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (e.type == DioExceptionType.connectionTimeout ||
          e.type == DioExceptionType.receiveTimeout ||
          e.type == DioExceptionType.sendTimeout) {
        return RegistrationResponse(
          success: false,
          error: 'Timeout Error',
          message: 'Connection timeout. Please try again.',
        );
      } else if (e.type == DioExceptionType.connectionError) {
        return RegistrationResponse(
          success: false,
          error: 'Connection Error',
          message:
              'Cannot connect to server. Please ensure your backend is running on $baseUrl.',
        );
      } else if (e.response?.statusCode == 400) {
        final errorData = e.response?.data;
        return RegistrationResponse.fromJson(errorData);
      } else {
        return RegistrationResponse(
          success: false,
          error: 'Server Error',
          message: 'Server error (${e.response?.statusCode})',
        );
      }
    } catch (e) {
      return RegistrationResponse(
        success: false,
        error: 'Unexpected Error',
        message: 'An unexpected error occurred: ${e.toString()}',
      );
    }
  }

  // New method to fetch application data
  Future<ApplicationResponse> fetchApplications() async {
    try {
      final response = await _dio.get(
        '/insurances',
        options: Options(
          followRedirects:
              false, // Temporarily disable redirects to inspect response
          validateStatus: (status) {
            return status != null &&
                status < 500; // Accept 3xx status codes for inspection
          },
        ),
      );

      // Log response details for debugging redirects
      if (response.statusCode != 200) {
        if (response.statusCode! >= 300 && response.statusCode! < 400) {
          throw Exception(
            'Server attempted a redirect (Status ${response.statusCode}) but missing Location header. Check backend configuration.',
          );
        } else if (response.statusCode == 403) {
          throw Exception(
            'Access Denied: You are not authorized to view applications. Please log in.',
          );
        } else {
          throw Exception(
            'Failed to load applications: Server error ${response.statusCode}.',
          );
        }
      }

      return ApplicationResponse.fromJson(response.data);
    } on DioException catch (e) {
      if (e.type == DioExceptionType.connectionError) {
        throw Exception(
          'Cannot connect to server. Please ensure your backend is running on $baseUrl.',
        );
      } else if (e.response?.statusCode == 403) {
        throw Exception(
          'Access Denied: You are not authorized to view applications. Please log in.',
        );
      }
      throw Exception(
        'Failed to load applications: ${e.response?.data['message'] ?? e.message}',
      );
    } catch (e) {
      throw Exception(
        'An unexpected error occurred while fetching applications: ${e.toString()}',
      );
    }
  }
}
