<#import "template.ftl" as t>
<html>
    <@t.head />
    <body>
        <div class="ui container" id="main" style="margin-top: 20px; margin-bottom: 20px;">
            <@t.navbar />

            <form class="ui form" id="form">
                <div class="field">
                    <label>
                        Username*
                        <input type="text" name="name" id='usr'>
                    </label>
                </div>
                <div class="field">
                    <label>
                        Password*
                        <input type="password" name="password" id="psd">
                    </label>
                </div>

                <hr/>
                <button class="ui primary button" type="submit">Login</button>
            </form>

            <button class="ui green button" onclick="register()">Register</button>

            <script>
                $('.dropdown').dropdown();

                function sendData(url) {
                    const XHR = new XMLHttpRequest();

                    // Bind the FormData object and the form element
                    const FD = new FormData(form);

                    // Define what happens on successful data submission
                    XHR.addEventListener("load", (event) => {
                        if(event.target.responseText=="success"){
                            window.location.href="index"
                        }else{
                            $.toast({
                                class:'error',
                                message: event.target.responseText,
                            });
                            $('#psd').val('')
                        }
                    });

                    // Define what happens in case of error
                    XHR.addEventListener("error", (event) => {
                        $.toast({
                            class:'error',
                            message: "Could not connect to login server",
                        });
                    });

                    // Set up our request
                    XHR.open("POST", url);

                    // The data sent is what the user provided in the form
                    XHR.send(FD);
                }
                
                function register(){
                    $('#psd').val(calculateMD5($('#usr').val()+":"+$('#psd').val()))

                    sendData("doRegister");
                    $.toast({
                      message: 'Register request sent... Please wait patiently!',
                    });
                }

                function calculateMD5(input) {
                    return CryptoJS.MD5(input).toString();
                }
                $('#form').submit(function(event){
                    event.preventDefault();
                    $('#psd').val(calculateMD5($('#usr').val()+":"+$('#psd').val()))

                    sendData("doLogin");
                    $.toast({
                      message: 'Login request sent... Please wait patiently!',
                    });
                })
            </script>
        </div>
    </body>
</html>