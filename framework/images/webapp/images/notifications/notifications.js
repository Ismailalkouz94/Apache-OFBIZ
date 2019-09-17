


function SuccessMessage(msg){
        $.smallBox({
                        title: "Success",
                        content: msg,
                        color: "#739E73",
                        iconSmall: "fa fa-check",
                        timeout: 5000
                    });
    
}

function ErrorMessage(msg){
      $.smallBox({
                        title: "Error",
                        content: msg,
                        color: "#C46A69",
                        iconSmall: "fa fa-times fa-2x fadeInRight animated",
                        timeout: 5000
                    });
    
}

function WarningMessage(msg){
     $.smallBox({
            title: "Warning",
            content: msg,
            color: "#dfb56c",
            timeout: 3000,
            icon: "fa fa-bell"
        });
    
}