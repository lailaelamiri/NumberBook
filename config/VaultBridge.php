<?php
class VaultBridge {
    private $serverHost = "localhost";
    private $vaultName = "ring_vault";
    private $vaultUser = "root";
    private $vaultPass = "";
    public $link;

    public function openLink() {
        $this->link = null;

        try {
            $this->link = new PDO(
                "mysql:host=" . $this->serverHost . ";dbname=" . $this->vaultName . ";charset=utf8mb4",
                $this->vaultUser,
                $this->vaultPass
            );
            $this->link->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $vaultError) {
            echo "Connection failed: " . $vaultError->getMessage();
        }

        return $this->link;
    }
}
?>
