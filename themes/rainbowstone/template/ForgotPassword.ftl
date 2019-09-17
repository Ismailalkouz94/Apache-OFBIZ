<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->
<div>
<div id="loginBar"><span>${uiLabelMap.CommonForgotYourPassword}?</span><div id="company-logo"></div></div>


<style>
.form-control{
width: 271px !important;
}
a, a:hover {
  color: black;
}
.container {
  max-width: 350px;
  padding: 15px;
  margin: 0 auto;
  border: 2px solid gray;
  -moz-border-radius: 15px;
  -webkit-border-radius: 15px;
  border-radius: 15px;
margin-top:200px;
}

.overlay {
  padding: 15px;
  background: rgba(255, 255, 255, 0.4);
  -moz-border-radius: 10px;
  -webkit-border-radius: 10px;
  border-radius: 10px;
  /*-webkit-filter: blur(5px);*/
}
h1 { color: black; }
.form-signin {
  color: black;
  font-family: 'Maven Pro', sans-serif;
}
.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}
.form-signin .checkbox { font-weight: normal; }
.form-signin .form-control {
  position: relative;
  height: auto;
  -webkit-box-sizing: border-box;
     -moz-box-sizing: border-box;
          box-sizing: border-box;
  font-size: 1.25em;
}
.form-signin .form-control:focus { z-index: 2; }

.rotate-45-left {
  -moz-transform: rotate(-45deg);
  -o-transform: rotate(-45deg);
  -webkit-transform: rotate(-45deg);
  transform: rotate(-45deg);
}
.rotate-45-right {
  -moz-transform: rotate(45deg);
  -o-transform: rotate(45deg);
  -webkit-transform: rotate(45deg);
  transform: rotate(45deg);
}
.footer {
  font-size: .75em;
  margin-top: 20px;
}
.header {
  font-family: 'Maven Pro', sans-serif;
  margin-bottom: 30px;
  text-align: center;
}
.loading {
  color: black;
  padding: 45px 15px;
  /*background: #eee;*/
  font-family: 'Maven Pro', sans-serif;
  text-align: center;
}

</style>
 <div class="container" style="display: none;">
      <div class="overlay loading">
        <i class="fa fa-spinner fa-5x fa-spin"></i><br/>
        <h1>Loading...</h1>
      </div>
    </div>
    
    <div class="container">
      <div class="overlay">

     <h1>   <span style="padding: 15px 0;font-size: 25px;color: #fff;font-weight: bold;"> ${uiLabelMap.CommonForgotYourPassword}? </span></h1>
        
    
        <form class="form-signin" role="form"  method="post" action="<@ofbizUrl>forgotPassword_step2</@ofbizUrl>" name="getSecurityQuestion">
    
          <div class="input-group"  style="margin-bottom: 3px;">
            <table class="basic-table">
             <tr class="input-group" style="margin-bottom: 3px;">
            <td> <span class="input-group-addon" style="height: 30px;"><i class="fa fa-user fa-fw"></i></span></td>
            <td> <input class="form-control" style="margin: 0px!important;height: 30px !important;" type="text" size="20" name="USERNAME" value="<#if requestParameters.USERNAME?has_content>${requestParameters.USERNAME}<#elseif autoUserLogin?has_content>${autoUserLogin.userLoginId}</#if>" placeholder="Username" required autofocus></td>
            </tr>
           </table>
          </div>
          <div style="margin-top:20px">
        <span style="margin-right:52px"> <button class="btn btn-lg btn-primary" style="width:40%" type="submit">${uiLabelMap.CommonContinue}</button></span> 
         <span>  <button class="btn btn-lg btn-primary" style="width:40%"  type="submit"><a href="<@ofbizUrl>login</@ofbizUrl>" style="color:white">${uiLabelMap.CommonGoBack}</a></button> </span>  
        <input type="hidden" name="JavaScriptEnabled" value="N"/>      
    </div>
 </form>

      </div>
    </div> <!-- /container -->
</div>