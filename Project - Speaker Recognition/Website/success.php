<?php
// Initialize the session
session_start();

// Check if the user is logged in, if not then redirect him to login page
if(!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true){
    header("location: login.php");
    exit;
    }
?>


<html>

<head>
    <title>Gotovo!</title>
    <link rel="icon" type="image/png" href="./resources/logo.ico"/>
    <link rel="stylesheet" type="text/css" href="./css/style.css">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="./js/script.js"></script>
</head>

<body>

    <div class="container">


        <div class="headline" onclick="location.href='logout.php';">Speaker Diarizator</div>

        <div class="content-container">

            <div class="welcome-msg">

<?php

    function recurse_copy($src,$dst) {
    $dir = opendir($src);
    @mkdir($dst);
    while(false !== ( $file = readdir($dir)) ) {
        if (( $file != '.' ) && ( $file != '..' )) {
            if ( is_dir($src . '/' . $file) ) {
                recurse_copy($src . '/' . $file,$dst . '/' . $file);
            }
            else {
                copy($src . '/' . $file,$dst . '/' . $file);
            }
        }
    }
        closedir($dir);
    }

    function create_zip($files = array(),$destination = '',$overwrite = false) {
            //if the zip file already exists and overwrite is false, return false
            if(file_exists($destination) && !$overwrite) { return false; }
            //vars
            $valid_files = array();
            //if files were passed in...
            if(is_array($files)) {
                //cycle through each file
                foreach($files as $file) {
                    //make sure the file exists
                    if(file_exists($file)) {
                        $valid_files[] = $file;
                    }
                }
            }
            //if we have good files...
            if(count($valid_files)) {
                //create the archive
                $zip = new ZipArchive();
                if($zip->open($destination,$overwrite ? ZIPARCHIVE::OVERWRITE : ZIPARCHIVE::CREATE) !== true) {
                    return false;
                }
                //add the files
                foreach($valid_files as $file) {
                    $zip->addFile($file,$file);
                }
                //debug
                //echo 'The zip archive contains ',$zip->numFiles,' files with a status of ',$zip->status;

                //close the zip -- done!
                $zip->close();

                //check to make sure the file exists
                return file_exists($destination);
            }
            else
            {
                return false;
            }
        }

        function deleteDir($dirPath) {
            if (! is_dir($dirPath)) {
                throw new InvalidArgumentException("$dirPath must be a directory");
            }
            if (substr($dirPath, strlen($dirPath) - 1, 1) != '/') {
                $dirPath .= '/';
            }
            $files = glob($dirPath . '*', GLOB_MARK);
            foreach ($files as $file) {
                if (is_dir($file)) {
                    deleteDir($file);
                } else {
                    unlink($file);
                }
            }
            rmdir($dirPath);
        }

        // Create user session initialize all files

        if (!empty($_SERVER['HTTP_CLIENT_IP'])) {
            $ip = $_SERVER['HTTP_CLIENT_IP'];
        } elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
            $ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
        } else {
            $ip = $_SERVER['REMOTE_ADDR'];
        }

        $ip = str_replace(':', '-', $ip);
        $ip = str_replace('/', '-', $ip);
        $ip = str_replace('.', '-', $ip);

        $rand = rand(0, 10000);
        // $session_name = 'session-' . (string)$ip . $rand;
        $session_name = 'session-' . $ip . '-' . $rand;
        $session_path = './sessions/' . $session_name;

        mkdir($session_path, 0777, true);

        recurse_copy('./src/', $session_path);

        // Upload the audio file into $target_dir

        $target_dir = $session_path . "/";
        $target_file = $target_dir . "showname.wav";
        $uploadOk = 1;
        $FileType = strtolower(pathinfo($target_dir . basename($_FILES["fileToUpload"]["name"]),PATHINFO_EXTENSION));

        // Check if file already exists
        if (file_exists($target_file)) {
            echo "Greška: Zapis već postoji.";
            $uploadOk = 0;
        }
        // Check file size
        if ($_FILES["fileToUpload"]["size"] > 80000000) {
            echo "Greška: Zapis je prevelik (80 MB max).";
            $uploadOk = 0;
        }
        // Allow certain file formats
        if($FileType != "wav") {
            echo "Greška: nepodržan format: ." . $FileType . " <b>(potreban .wav)</b>";
            $uploadOk = 0;
        }
        // Check if $uploadOk is set to 0 by an error
        if ($uploadOk == 1) {
            if (!move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
                echo "Greška kod učitavanja zapisa.";
                $uploadOk = 0;
            }
            $zipname = preg_replace('/\\.[^.\\s]{3,4}$/', '', $_FILES["fileToUpload"]["name"]);
        }

        if($uploadOk == 1) {

            $cmd_lium = 'java -Xmx2024m -jar ' . $session_path . '/lium.jar  --fInputMask=' . $session_path . '/showname.wav --sOutputMask=' . $session_path . '/showname.seg --doCEClustering  showname';

            $cmd_sep = 'java -jar ./Separator.jar';

            exec($cmd_lium);

            chdir($session_path . '/');
            exec($cmd_sep);

            // Zip files

            $files_to_zip = array();

            $files = scandir('./');

            foreach($files as $file) {
                if(substr($file, 0, 5) === "showN") {
                    array_push($files_to_zip, './' . $file);
                }
            }

            $result = create_zip($files_to_zip, $zipname . '.zip');

        }

        ?>

            </div>

            <?php if($uploadOk == 1){
                echo '
                <div class="welcome-msg">
                    U audio-zapisu <b>' . $zipname . '.wav</b> je bilo ' . count($files_to_zip) . ' govornika.
                </div>

                <div class="upload-container">
                    <a href="' . $session_path . '/' . $zipname .'.zip">
                    <input type="submit" value="Preuzmi .zip" name="submit" class="download-button">
                    </a>
                </div>';
            }?>

            <a href="./index.php" style="text-decoration: none;">
                <div class="back-btn">
                    Povratak
                </div>
            </a>

        </div>

    </div>




</body>
</html>
