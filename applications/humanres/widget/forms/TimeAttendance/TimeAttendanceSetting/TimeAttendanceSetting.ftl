<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
            <header>
                <h2>Time Attendance Settings</h2>
                </header>
            <div role="content">
                <form method="post" class="smart-form" name="setTimeAttendanceSetting" action="<@ofbizUrl>setTimeAttendanceSetting</@ofbizUrl>" class="search_bar">
                    <table class="basic-table">
                        <tr>             
                            <td>
                                <label style="vertical-align: sub;">Start Work </label>
                                </td>
                            <td>			    
                                <fieldset>
                                    <label class="input"> <i class="icon-append fa fa-clock-o"></i>
                                        <input name="StartWork" type="text" class="form-control time" id="StartWork"/>
                                        </label>	
                                    </fieldset>
                                </td>
                            <td>
                                <label style="vertical-align: sub;">to Late </label>
                                </td>
                            <td>    
                                <fieldset>
                                    <label class="input"> <i class="icon-append fa fa-clock-o"></i>
                                        <input name="toLate" type="text" class="form-control time" id="toLate"/>
                                        </label>	
                                    </fieldset>
                                </td>
                            </tr>
                        <tr>             
                            <td>
                                <label style="vertical-align: sub;">End Work </label>
                                </td>
                            <td>			    
                                <fieldset>
                                    <label class="input"> <i class="icon-append fa fa-clock-o"></i>
                                        <input name="EndWork" type="text" class="form-control time" id="EndWork"/>
                                        </label>	
                                    </fieldset>
                                </td>
                            <td>
                                <label style="vertical-align: sub;">Befor End Work </label>
                                </td>
                            <td>		    
                                <fieldset>
                                    <label class="input"> <i class="icon-append fa fa-clock-o"></i>
                                        <input name="BeforEndWork" type="text" class="form-control time" id="BeforEndWork"/>
                                        </label>	
                                    </fieldset>
                                </td>
                            </tr>
                        <tr>  
                            <td>
                                <label style="vertical-align: sub;">First OFF Day </label>
                                </td>
                            <td >
                                <!--<input name="FirstOFFDay" type="week" class="form-control" id="FirstOFFDay"></select>-->
                                <select name="FirstOFFDay" id="FirstOFFDay">
                                    <!--<option value="" selected></option>-->
                                    <option value="null"> </option>
                                    <option value="Monday">Monday</option>
                                    <option value="Tuesday">Tuesday</option>
                                    <option value="Wednesday">Wednesday</option>
                                    <option value="Thursday">Thursday</option>
                                    <option value="Friday">Friday</option>
                                    <option value="Saturday">Saturday</option>
                                    <option value="Sunday">Sunday</option>
                                    </select>
                                </td>
                            <td>
                                <label style="vertical-align: sub;">Second OFF Day </label>
                                </td>
                            <td>
                                <!--<input name="SecondOFFDay" type="week" class="form-control" id="SecondOFFDay"></select>-->
                                <select name="SecondOFFDay" id="SecondOFFDay">
                                    <!--<option value="" selected></option>-->
                                    <option value="null"> </option>
                                    <option value="Tuesday">Tuesday</option>
                                    <option value="Wednesday">Wednesday</option>
                                    <option value="Thursday">Thursday</option>
                                    <option value="Friday">Friday</option>
                                    <option value="Saturday">Saturday</option>
                                    <option value="Sunday">Sunday</option>
                                    </select>
                                </td>
                            </tr>
                        
                        
                        <tr>
                            <td>
                                <label>Leave Hours </label>
                                </td>
                            <td>
                                <input class="form-control" name="leaveHours"  id="leaveHours"> </select>
                                </td>
                            </tr>
                        
                        
                        
                        <tr>
                            <td>
                                <label>Time Attendence Allowence ID </label>
                                </td>
                            <td>
                                <select class="form-control" name="AllowenceID"  id="AllowenceID"> </select>
                                </td>
                            </tr>
                        </table>
                    <button class="btn btn-primary1"  type="submit" id="submit_button" >Save</button>
                    </form> 
                </div>
        </article>
    </div>
</div>

<script>     
    <#include "component://humanres/widget/forms/TimeAttendance/TimeAttendanceSetting/TimeAttendanceSetting.js"/>
    </script>
