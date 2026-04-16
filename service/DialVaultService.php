<?php
require_once __DIR__ . '/../config/VaultBridge.php';

class DialVaultService {
    private $link;
    private $targetTable = "dial_entry";

    public function __construct() {
        $bridge = new VaultBridge();
        $this->link = $bridge->openLink();
    }

    public function storeEntry($displayName, $dialNumber, $entryOrigin = "mobile") {
        $query = "INSERT INTO " . $this->targetTable . " (display_name, dial_number, entry_origin)
                  VALUES (:displayName, :dialNumber, :entryOrigin)";
        $prepared = $this->link->prepare($query);
        return $prepared->execute([
            ':displayName' => $displayName,
            ':dialNumber'  => $dialNumber,
            ':entryOrigin' => $entryOrigin
        ]);
    }

    public function fetchAllEntries() {
        $query = "SELECT * FROM " . $this->targetTable . " ORDER BY display_name ASC";
        $prepared = $this->link->prepare($query);
        $prepared->execute();
        return $prepared->fetchAll(PDO::FETCH_ASSOC);
    }

    public function lookupByKeyword($searchTerm) {
        $query = "SELECT * FROM " . $this->targetTable . "
                  WHERE display_name LIKE :searchTerm OR dial_number LIKE :searchTerm
                  ORDER BY display_name ASC";
        $prepared = $this->link->prepare($query);
        $prepared->execute([
            ':searchTerm' => '%' . $searchTerm . '%'
        ]);
        return $prepared->fetchAll(PDO::FETCH_ASSOC);
    }
    public function lookupWithStats($searchTerm) {
    $query = "SELECT display_name, dial_number, COUNT(*) as occurrence
              FROM " . $this->targetTable . "
              WHERE display_name LIKE :searchTerm OR dial_number LIKE :searchTerm
              GROUP BY display_name, dial_number
              ORDER BY occurrence DESC";
    $prepared = $this->link->prepare($query);
    $prepared->execute([':searchTerm' => '%' . $searchTerm . '%']);
    $rows = $prepared->fetchAll(PDO::FETCH_ASSOC);

    $total = array_sum(array_column($rows, 'occurrence'));

    foreach ($rows as &$row) {
        $row['percentage'] = $total > 0
            ? round(($row['occurrence'] / $total) * 100, 1)
            : 0;
    }

    return $rows;
}
}
?>
