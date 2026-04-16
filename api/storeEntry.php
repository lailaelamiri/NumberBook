<?php
header("Content-Type: application/json");
require_once __DIR__ . '/../service/DialVaultService.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $incoming = json_decode(file_get_contents("php://input"), true);

    if (!isset($incoming['display_name']) || !isset($incoming['dial_number'])) {
        echo json_encode([
            "success" => false,
            "message" => "Missing required fields"
        ]);
        exit;
    }

    $vaultService = new DialVaultService();
    $stored = $vaultService->storeEntry($incoming['display_name'], $incoming['dial_number'], "mobile");

    echo json_encode([
        "success" => $stored,
        "message" => $stored ? "Entry saved to vault" : "Vault storage failed"
    ]);
}
?>
