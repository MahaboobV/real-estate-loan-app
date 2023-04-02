$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    const accessCode = urlParams.get('code');
    ajaxPost(accessCode);
});
function ajaxPost(accessCode) {
   $.ajax({
            type : "GET",
            contentType : "application/json",
            url : "/getToken",
            data : {
             code : accessCode
            },
            success : function(result) {
            alert(JSON.stringify(result));
                if (result != null) {
                    let jwtToken = result.token;
                    let view = result.viewName;
                    alert(jwtToken);
                    localStorage.setItem("JwtToken", 'Bearer ' + jwtToken);
                    triggerRestCall(view);

                }
            }

    });
}

function triggerRestCall(view) {
alert(localStorage.getItem('JwtToken'));
    if(view == 'adviser'){
        $.ajax({
                    type : "GET",
                    contentType : "application/json",
                    headers: {
                            "Authorization": localStorage.getItem('JwtToken')
                    },
                    url : "/loanapp/allcustomerloandetails",
                    success : function(result) {
                       if(result != null) {

                       }
                    }
        });
    }else {
        $.ajax({
                           type : "GET",
                           contentType : "application/json",
                           url : "/customerloanform",
                           headers: {
                             "Authorization": localStorage.getItem('JwtToken')
                           },
                           success : function(data) {
                              if(data) {
                                window.location.href = "customer-loan-application";
                              }
                           }
               });
    }
}