<#import "template.ftl" as t>
<html>
    <@t.head />
    <body>
        <div class="ui container" id="main" style="margin-top: 20px; margin-bottom: 20px;">
            <@t.navbar />

            <div class="ui segment">
                <a class="ui primary <#if username??><#else>disabled</#if> button" tabindex="0" href="add">Add</a>
                <a class="ui red button"  tabindex="0" href="reload">Reload</a>

                <label>
                    <abbr title="Completely hide all completed tasks and fold other tasks">Compact Mode</abbr>
                    <input type="checkbox" id="compact" tabindex="0" onchange="changeCheckbox()">
                </label>
            </div>

            <div class="ui pink segment">
                <h1><i class="icon fire"></i>Newly Added</h1>
                <div class="ui comments">
                    <#list model.newlyAdded as em>
                        <#if em.isVisibleTo(user)>
                            <div class="comment">

                                <a class="avatar">
                                    <i class="fire icon"></i>
                                </a>

                                <div class="content">
                                    <a class="author">${em.uploader}</a>
                                    <div class="metadata">
                                        <div class="date">${em.addDate}</div>
                                    </div>
                                    <div class="text">Added <a href="#box_${em.internalID}">${em.name}</a> to ${em.getVisibilityTag()}</div>
                                </div>
                            </div>
                        </#if>
                    </#list>
                </div>
            </div>

            <div class="ui red segment">
                <h1><i class="icon exclamation triangle"></i>Emergency Events</h1>
                <#list model.emergencies as em>
                    <@t.ddl em/>
                </#list>
            </div>

            <div class="ui yellow segment">
                <h1><i class="icon clock"></i>Ongoing Events</h1>
                <#list model.ongoing as em>
                    <@t.ddl em/>
                </#list>
            </div>

            <div class="ui blue segment">
                <h1><i class="icon bell"></i>Upcoming Events</h1>
                <#list model.future as em>
                    <@t.ddl em/>
                </#list>
            </div>

            <div class="ui grey segment">
                <h1><i class="icon bell slash"></i>Past Events</h1>
                <#list model.ended as em>
                    <@t.ddl em/>
                </#list>
            </div>

            <@t.footer />
        </div>

        <script>
            $('#compact').prop('checked',${compact?string('true', 'false')});
            function changeCheckbox(){
                console.log("H")
                location.href="?compact="+$('#compact').is(":checked")
            }

            $('.ui.accordion').accordion();
            $('.popup')
              .popup({
                on: 'focus'
              });
        </script>
    </body>
</html>