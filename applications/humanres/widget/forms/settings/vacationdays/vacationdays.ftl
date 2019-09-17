<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
            <header>
                <h2>Vacation Days</h2>
            </header>
            <div role="content">
                <form method="post" class="smart-form" name="setEmployeeSettings" class="search_bar">
                    <table class="basic-table">
                        <tr>
                            <td>
                                <label style="vertical-align: sub;">Sick Vacation </label>
                            </td>
                            <td>
                                <label class="input">
                                    <input name="sickVacation" type="text" class="form-control" id="sickVacation"/>
                                </label>
                            </td>

                            <td>
                            <button class="btn btn-primary1" type="button" id="submit_button" onclick="setSickVacation()">Save
                            </button>
                            </td>

                        </tr>
                        <tr>
                            <td>
                                <label style="vertical-align: sub;">Sick Vacation (Surgery)</label>
                            </td>
                            <td>
                                <label class="input">
                                    <input name="sickVacationSurgery" type="text" class="form-control"
                                           id="sickVacationSurgery"/>
                                </label>
                            </td>

                            <td>
                            <button class="btn btn-primary1" type="button" id="submit_button" onclick="setSickVacationSurgery()">Save
                            </button>
                            </td>

                        </tr>
                    </table>

                </form>
            </div>
    </article>
</div>
</div>

<script>
    <#include "component://humanres/widget/forms/settings/vacationdays/vacationdays.js"/>
</script>
<style>
    .jarviswidget tbody tr:last-child td {
    border-bottom: none;
    vertical-align: inherit;
     }
     td, th {
    vertical-align: inherit;
    }
</style>