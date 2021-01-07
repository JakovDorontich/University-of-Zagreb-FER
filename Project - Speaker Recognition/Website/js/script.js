var DOM = {
        loadingContainer: '.loading-container',
        uploadButton: '.upload-button',
        welcomeContainer: '.welcome-container'
};


window.onload=function(){
  
    var uploadStarted = function() {
    
        document.querySelector(DOM.loadingContainer).classList.toggle('hidden');
        document.querySelector(DOM.welcomeContainer).classList.toggle('hidden');
    
    };

    document.querySelector(DOM.uploadButton).addEventListener('click', uploadStarted);
    
}