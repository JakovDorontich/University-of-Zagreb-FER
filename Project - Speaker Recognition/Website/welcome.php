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
    <title>Speaker Diarizator</title>
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

            <div class="loading-container hidden">
                <div class="loading-msg">
                    Molimo pričekajte...
                </div>
                <div class="loading-icon">
                    <img src="./resources/loading.gif" width="90px">
                </div>
            </div>

            <div class="welcome-container">
			    <div class="welcome-msg">
                    Prilagodite audio-zapis ovdije: <a href="https://audio.online-convert.com/convert-to-wav" target="_blank">link</a> <b> 16 bit, 16000 Hz, mono</b>
                </div>
                <div class="welcome-msg">
                    Odaberite audio-zapis u .WAV formatu.
                </div>
                <div class="upload-container">
                    <form action="success.php" method="post" enctype="multipart/form-data">
                    <input type="file" name="fileToUpload" id="fileToUpload"><br>
                    <input type="submit" value="Pokreni" name="submit" class="upload-button">
                    </form>
                </div>
            </div>

        </div>

    </div>

</body>
</html>
