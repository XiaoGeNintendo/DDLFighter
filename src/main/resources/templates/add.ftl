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
                        Visibility*
                        <div class="ui selection dropdown">
                            <input type="hidden" name="visibility">
                            <i class="dropdown icon"></i>
                            <div class="default text">Choose Visibility</div>
                            <div class="menu">
                                <div class="item" data-value="2">Private - Only you can see</div>
                                <div class="item" data-value="1">Protected - People in certain groups can see</div>
                                <div class="item" data-value="0">Public - Everyone can see</div>
                            </div>
                        </div>
                    </div>

                    <div class="field">
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
                    </div>
                </div>

                <div class="field">
                    <label>
                        Tags
                        <input type="text" name="tag" placeholder="Use , to separate. eg: math, homework, hurry up">
                    </label>
                </div>
                <div class="field">
                    Importance*
                    <div class="ui selection dropdown">
                        <input type="hidden" name="importance">
                        <i class="dropdown icon"></i>
                        <div class="default text">Choose Importance</div>
                        <div class="menu">
                            <#list importances as importance>
                                <div class="item" data-value="${importance.ordinal()}">${importance}</div>
                            </#list>
                        </div>
                    </div>
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