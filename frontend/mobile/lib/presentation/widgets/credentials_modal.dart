import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../data/models/saved_credential.dart';
import '../../data/services/storage_service.dart';

class CredentialsModal extends StatelessWidget {
  final Function(String username, String password) onCredentialSelected;

  const CredentialsModal({super.key, required this.onCredentialSelected});

  @override
  Widget build(BuildContext context) {
    final credentials = StorageService.to.getSavedCredentials();

    return Container(
      decoration: const BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          // Handle bar
          Container(
            margin: const EdgeInsets.only(top: 12),
            width: 40,
            height: 4,
            decoration: BoxDecoration(
              color: Colors.grey[300],
              borderRadius: BorderRadius.circular(2),
            ),
          ),

          // Header
          Padding(
            padding: const EdgeInsets.all(20),
            child: Row(
              children: [
                const Icon(Icons.account_circle, color: Colors.blue),
                const SizedBox(width: 12),
                const Text(
                  'Saved Accounts',
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
                const Spacer(),
                IconButton(
                  onPressed: () => Get.back(),
                  icon: const Icon(Icons.close),
                ),
              ],
            ),
          ),

          // Credentials list
          if (credentials.isEmpty)
            const Padding(
              padding: EdgeInsets.all(40),
              child: Column(
                children: [
                  Icon(
                    Icons.account_circle_outlined,
                    size: 64,
                    color: Colors.grey,
                  ),
                  SizedBox(height: 16),
                  Text(
                    'No saved accounts',
                    style: TextStyle(fontSize: 16, color: Colors.grey),
                  ),
                  SizedBox(height: 8),
                  Text(
                    'Enable "Remember me" when logging in to save your credentials',
                    textAlign: TextAlign.center,
                    style: TextStyle(fontSize: 14, color: Colors.grey),
                  ),
                ],
              ),
            )
          else
            Flexible(
              child: ListView.builder(
                shrinkWrap: true,
                itemCount: credentials.length,
                itemBuilder: (context, index) {
                  final credential = credentials[index];
                  return ListTile(
                    leading: CircleAvatar(
                      backgroundColor: Colors.blue[100],
                      child: Text(
                        credential.username.substring(0, 1).toUpperCase(),
                        style: TextStyle(
                          color: Colors.blue[700],
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    title: Text(
                      credential.username,
                      style: const TextStyle(fontWeight: FontWeight.w600),
                    ),
                    subtitle: Text(
                      'Saved ${_formatDate(credential.savedAt)}',
                      style: TextStyle(color: Colors.grey[600], fontSize: 12),
                    ),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        IconButton(
                          onPressed: () => _showDeleteConfirmation(credential),
                          icon: Icon(
                            Icons.delete_outline,
                            color: Colors.red[400],
                          ),
                        ),
                        const Icon(Icons.chevron_right),
                      ],
                    ),
                    onTap: () {
                      onCredentialSelected(
                        credential.username,
                        credential.password,
                      );
                      Get.back();
                    },
                  );
                },
              ),
            ),

          // Clear all button
          if (credentials.isNotEmpty)
            Padding(
              padding: const EdgeInsets.all(20),
              child: TextButton.icon(
                onPressed: _showClearAllConfirmation,
                icon: const Icon(Icons.clear_all, color: Colors.red),
                label: const Text(
                  'Clear All Saved Accounts',
                  style: TextStyle(color: Colors.red),
                ),
              ),
            ),

          // Bottom padding for safe area
          SizedBox(height: MediaQuery.of(context).padding.bottom),
        ],
      ),
    );
  }

  String _formatDate(DateTime date) {
    final now = DateTime.now();
    final difference = now.difference(date);

    if (difference.inDays == 0) {
      return 'today';
    } else if (difference.inDays == 1) {
      return 'yesterday';
    } else if (difference.inDays < 7) {
      return '${difference.inDays} days ago';
    } else {
      return '${date.day}/${date.month}/${date.year}';
    }
  }

  void _showDeleteConfirmation(SavedCredential credential) {
    Get.dialog(
      AlertDialog(
        title: const Text('Delete Account'),
        content: Text(
          'Are you sure you want to remove "${credential.username}" from saved accounts?',
        ),
        actions: [
          TextButton(onPressed: () => Get.back(), child: const Text('Cancel')),
          TextButton(
            onPressed: () async {
              await StorageService.to.removeCredential(credential.username);
              Get.back();
              Get.back(); // Close modal
              Get.snackbar(
                'Account Removed',
                '${credential.username} has been removed from saved accounts',
                snackPosition: SnackPosition.BOTTOM,
              );
            },
            child: const Text('Delete', style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }

  void _showClearAllConfirmation() {
    Get.dialog(
      AlertDialog(
        title: const Text('Clear All Accounts'),
        content: const Text(
          'Are you sure you want to remove all saved accounts? This action cannot be undone.',
        ),
        actions: [
          TextButton(onPressed: () => Get.back(), child: const Text('Cancel')),
          TextButton(
            onPressed: () async {
              await StorageService.to.clearAllCredentials();
              Get.back();
              Get.back(); // Close modal
              Get.snackbar(
                'All Accounts Cleared',
                'All saved accounts have been removed',
                snackPosition: SnackPosition.BOTTOM,
              );
            },
            child: const Text('Clear All', style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }
}
