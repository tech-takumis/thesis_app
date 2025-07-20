class ApplicationResponse {
  final List<ApplicationContent> content;
  final int page;
  final int size;
  final int totalElements;
  final int totalPages;
  final bool last;
  final bool first;
  final String? next;
  final String? prev;

  ApplicationResponse({
    required this.content,
    required this.page,
    required this.size,
    required this.totalElements,
    required this.totalPages,
    required this.last,
    required this.first,
    this.next,
    this.prev,
  });

  factory ApplicationResponse.fromJson(Map<String, dynamic> json) {
    return ApplicationResponse(
      content:
          (json['content'] as List)
              .map((i) => ApplicationContent.fromJson(i))
              .toList(),
      page: json['page'],
      size: json['size'],
      totalElements: json['totalElements'],
      totalPages: json['totalPages'],
      last: json['last'],
      first: json['first'],
      next: json['next'],
      prev: json['prev'],
    );
  }
}

class ApplicationContent {
  final int id; // Added id field
  final String displayName;
  final String description;
  final bool requiredAiAnalyses;
  final List<ApplicationField> fields;

  ApplicationContent({
    required this.id, // Required in constructor
    required this.displayName,
    required this.description,
    required this.requiredAiAnalyses,
    required this.fields,
  });

  factory ApplicationContent.fromJson(Map<String, dynamic> json) {
    return ApplicationContent(
      id: json['id'], // Parse id from JSON
      displayName: json['displayName'],
      description: json['description'],
      requiredAiAnalyses: json['requiredAiAnalyses'],
      fields:
          (json['fields'] as List)
              .map((i) => ApplicationField.fromJson(i))
              .toList(),
    );
  }
}

class ApplicationField {
  final int id;
  final String key;
  final String fieldType;
  final String displayName;
  final String note;
  final bool hasCoordinate;
  final bool required;

  ApplicationField({
    required this.id,
    required this.key,
    required this.fieldType,
    required this.displayName,
    required this.note,
    required this.hasCoordinate,
    required this.required,
  });

  factory ApplicationField.fromJson(Map<String, dynamic> json) {
    return ApplicationField(
      id: json['id'],
      key: json['key'],
      fieldType: json['fieldType'],
      displayName: json['displayName'],
      note: json['note'],
      hasCoordinate: json['hasCoordinate'],
      required: json['_required'],
    );
  }
}
