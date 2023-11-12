<#import "template.ftl" as t>
<html>
    <@t.head />
    <body>
        <div class="ui container" id="main" style="margin-top: 20px; margin-bottom: 20px;">
            <@t.navbar />

            <form class="ui form" action="doAdd" method="post">
                <div class="field">
                    <label>
                        Event name*
                        <input type="text" name="name" placeholder="Calculus Homework">
                    </label>
                </div>
                <div class="fields">
                    <div class="field">
                        <label>
                            Start time*
                            <input type="datetime-local" name="starttime">
                        </label>
                    </div>
                    <div class="field">
                        <label>
                            <abbr title="Leave blank if it is a DDL">End time</abbr>
                            <input type="datetime-local" name="endtime">
                        </label>
                    </div>
                </div>


                <div class="fields">
                    <div class="field">
                        <label>
                            Visibility*
                            <select class="ui dropdown" name="visibility">
                                <option value="2">Private - Only you can see</option>
                                <option value="1">Protected - People in certain groups can see</option>
                                <option value="0">Public - Everyone can see</option>
                            </select>
                        </label>
                    </div>

                    <div class="field">
                        <label>
                            <abbr title="Only required when visibility is protected">Visible Groups</abbr>
                            <div class="ui clearable multiple search selection dropdown">
                                <input type="hidden" name="group">
                                <i class="dropdown icon"></i>
                                <div class="default text">Select Groups</div>
                                <div class="menu">
                                    <#list user.getGroupsD() as gp>
                                        <div class="item" data-value="${gp.id}">${gp.name}</div>
                                    </#list>
                                </div>
                            </div>
                        </label>
                    </div>
                </div>

                <div class="field">
                    <label>
                        Tags
                        <input type="text" name="tag" placeholder="Use , to separate. eg: math, homework, hurry up">
                    </label>
                </div>
                <div class="field">
                    <label>
                        Importance*
                        <select class="ui dropdown" name="importance">
                            <#list importances as importance>
                                <option value="${importance.ordinal()}">${importance}</option>
                            </#list>
                        </select>
                    </label>
                </div>
                <div class="desc">
                    <label>
                        Description
                        <textarea name="desc" placeholder="Enter description of the event"></textarea>
                    </label>
                </div>
                <hr/>
                <button class="ui primary button" type="submit">Submit</button>
            </form>

            <script>
                $('.dropdown').dropdown();
            </script>
        </div>
    </body>
</html>