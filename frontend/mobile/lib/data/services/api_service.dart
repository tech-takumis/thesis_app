import 'dart:convert';
import 'package:dio/dio.dart';
import 'package:get/get.dart' as getx;
import 'package:image_picker/image_picker.dart'; // Import XFile
import 'package:http_parser/http_parser.dart'; // Import for MediaType
import '../models/auth_response.dart';
import '../models/login_request.dart';
import '../models/registration_request.dart';
import '../models/registration_response.dart';
import '../models/application_data.dart';
import '../models/application_submission_response.dart'; // Import new model
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
          'Content-Type': 'application/json', // Default for most requests
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
        logPrint: (obj) => print('🌐 API: $obj'),
      ),
    );

    _dio.interceptors.add(
      InterceptorsWrapper(
        onRequest: (options, handler) async {
          // Add JWT token to requests if available
          final token = StorageService.to.getToken();
          if (token != null && token.isNotEmpty) {
            options.headers['Authorization'] = 'Bearer $token';
            print(
              '🌐 API: Adding Authorization header: Bearer ...${token.substring(token.length - 20)}',
            );
          }
          return handler.next(options);
        },
        onError: (error, handler) {
          print('🚨 API Error: ${error.message}');
          print('🚨 API Error Type: ${error.type}');
          print('🚨 API Error Response: ${error.response?.data}');
          handler.next(error);
        },
      ),
    );
  }

  Future<AuthResponse> login(LoginRequest request) async {
    try {
      print('🚀 Attempting login to: $baseUrl/mobile/login');

      final response = await _dio.post('/mobile/login', data: request.toJson());

      print('✅ Login successful: ${response.statusCode}');
      return AuthResponse.fromJson(response.data);
    } on DioException catch (e) {
      print('❌ Login failed: ${e.message}');

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
      print('❌ Unexpected login error: $e');
      return AuthResponse(
        message: 'An unexpected error occurred: ${e.toString()}',
        success: false,
      );
    }
  }

  Future<RegistrationResponse> register(RegistrationRequest request) async {
    try {
      print('🚀 Attempting registration to: $baseUrl/farmers');

      final response = await _dio.post('/farmers', data: request.toJson());

      print('✅ Registration successful: ${response.statusCode}');
      return RegistrationResponse.fromJson(response.data);
    } on DioException catch (e) {
      print('❌ Registration failed: ${e.message}');

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
      print('❌ Unexpected registration error: $e');
      return RegistrationResponse(
        success: false,
        error: 'Unexpected Error',
        message: 'An unexpected error occurred: ${e.toString()}',
      );
    }
  }

  // Method to fetch application data
  Future<ApplicationResponse> fetchApplications() async {
    try {
      print('🚀 Fetching applications from: $baseUrl/insurances');

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
        print('⚠️ Server responded with status code: ${response.statusCode}');
        print('⚠️ Response Headers: ${response.headers}');
        print('⚠️ Response Data: ${response.data}');
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

      print('✅ Applications fetched: ${response.statusCode}');
      return ApplicationResponse.fromJson(response.data);
    } on DioException catch (e) {
      print('❌ Fetch applications failed: ${e.message}');
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
      print('❌ Unexpected applications fetch error: $e');
      throw Exception(
        'An unexpected error occurred while fetching applications: ${e.toString()}',
      );
    }
  }

  // New method to submit application form
  Future<ApplicationSubmissionResponse> submitApplicationForm(
    int applicationId, // Changed to use int id
    Map<String, dynamic> fieldValues,
    Map<String, XFile> files,
  ) async {
    try {
      print('🚀 Attempting to submit application form for ID: $applicationId');

      final formData = FormData();

      // Add fieldValues as a JSON string part with explicit content type
      formData.files.add(
        MapEntry(
          'fieldValues',
          MultipartFile.fromString(
            jsonEncode(fieldValues),
            contentType: MediaType(
              'application',
              'json',
            ), // Explicitly set content type
          ),
        ),
      );
      print(
        '🌐 API: fieldValues: ${jsonEncode(fieldValues)} (as application/json part)',
      );

      // Add files as multipart files, all under the "files" key
      for (var entry in files.entries) {
        final file = entry.value;
        formData.files.add(
          MapEntry(
            "files", // This is the crucial change: use "files" as the part name
            await MultipartFile.fromFile(file.path, filename: file.name),
          ),
        );
        print('🌐 API: Added file: ${file.name} under part "files"');
      }

      // Corrected endpoint: /insurances/{id}/application:submit
      final response = await _dio.post(
        '/insurances/$applicationId/application:submit', // Using ID in the endpoint
        data: formData,
      );

      print('✅ Application submission successful: ${response.statusCode}');

      // Backend now consistently returns a JSON object for success or failure
      return ApplicationSubmissionResponse.fromJson(response.data);
    } on DioException catch (e) {
      print('❌ Application submission failed: ${e.message}');
      String errorMessage = 'An unexpected error occurred.';
      String? errorType;

      if (e.type == DioExceptionType.connectionTimeout ||
          e.type == DioExceptionType.receiveTimeout ||
          e.type == DioExceptionType.sendTimeout) {
        errorMessage =
            'Connection timeout. Please check your network connection.';
        errorType = 'Timeout Error';
      } else if (e.type == DioExceptionType.connectionError) {
        errorMessage =
            'Cannot connect to server. Please ensure your backend is running on $baseUrl.';
        errorType = 'Connection Error';
      } else if (e.response?.statusCode == 400) {
        final errorData = e.response?.data;
        if (errorData is Map<String, dynamic> &&
            errorData.containsKey('message')) {
          errorMessage = errorData['message'];
        } else if (errorData is String) {
          errorMessage = errorData; // Sometimes 400 can return a plain string
        } else {
          errorMessage = 'Invalid input data.';
        }
        errorType = 'Bad Request';
      } else if (e.response?.statusCode == 403) {
        errorMessage =
            'Access Denied: You are not authorized to submit this application. Please log in.';
        errorType = 'Forbidden';
      } else if (e.response?.statusCode != null) {
        // Generic server error with status code
        final errorData = e.response?.data;
        if (errorData is Map<String, dynamic> &&
            errorData.containsKey('message')) {
          errorMessage =
              'Server error (${e.response?.statusCode}): ${errorData['message']}';
        } else if (errorData is String) {
          errorMessage = 'Server error (${e.response?.statusCode}): $errorData';
        } else {
          errorMessage = 'Server error (${e.response?.statusCode}).';
        }
        errorType = 'Server Error';
      } else {
        // Other Dio errors (e.g., unknown, cancel)
        errorMessage = e.message ?? 'An unknown network error occurred.';
        errorType = 'Network Error';
      }

      return ApplicationSubmissionResponse(
        success: false,
        message: errorMessage,
        error: errorType,
      );
    } catch (e) {
      print('❌ Unexpected application submission error: $e');
      return ApplicationSubmissionResponse(
        success: false,
        message: 'An unexpected error occurred: ${e.toString()}',
        error: 'Unexpected Error',
      );
    }
  }
}
