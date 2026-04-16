<?php
header("Content-Type: application/json");
require_once __DIR__ . '/../service/DialVaultService.php';

$vaultService = new DialVaultService();
$allEntries = $vaultService->fetchAllEntries();

echo json_encode($allEntries);
?>
