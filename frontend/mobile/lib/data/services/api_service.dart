// import 'dart:convert';
import 'package:dio/dio.dart';
import 'package:get/get.dart' as getx;
import '../models/auth_response.dart';
import '../models/login_request.dart';
import '../models/registration_request.dart';
import '../models/registration_response.dart';

class ApiService extends getx.GetxService {
  late Dio _dio;
  static const String baseUrl = 'http://localhost:8000';

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

    // Add interceptors for logging
    _dio.interceptors.add(
      LogInterceptor(
        requestBody: true,
        responseBody: true,
        requestHeader: true,
        responseHeader: false,
        error: true,
        logPrint: (obj) => print('🌐 API: $obj'),
      ),
    );

    // Add error interceptor
    _dio.interceptors.add(
      InterceptorsWrapper(
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
              'Cannot connect to server. Please check if the server is running on localhost:8000.',
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
      final response = await _dio.post(
        '/api/v1/register/farmers',
        data: request.toJson(),
      );

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
              'Cannot connect to server. Please check if the server is running.',
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
}
