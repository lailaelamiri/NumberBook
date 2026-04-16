<?php
header("Content-Type: application/json");
require_once __DIR__ . '/../service/DialVaultService.php';

if (!isset($_GET['searchTerm'])) {
    echo json_encode([]);
    exit;
}

$searchTerm = $_GET['searchTerm'];

$vaultService = new DialVaultService();
$matches = $vaultService->lookupWithStats($searchTerm);

echo json_encode($matches);
?>