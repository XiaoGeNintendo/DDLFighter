<#import "template.ftl" as t>
<html xmlns="http://www.w3.org/1999/html">
    <@t.head />
    <body>
        <div class="ui container" id="main" style="margin-top: 20px; margin-bottom: 20px;">
            <@t.navbar />

            <#if username??>
                <form action="addGroup" method="post">
                    <label>
                        Group Name
                        <input type="text" name="name" placeholder="Calculus Class 1" />
                    </label>
                    <input type="submit" class="ui primary button" value="Create a group"/>
                </form>
            </#if>

            <table class="ui celled table">
                <thead>
                    <tr>
                        <th style="width:60%">Name</th>
                        <th>Creator</th>
                        <th>Created</th>
                        <th>Users</th>
                        <th>Join</th>
                    </tr>
                </thead>
                <tbody>
                <#list model.userGroups() as g>
                    <tr>
                        <td>
                            <a onclick="showModal('${g.id}')" href="#" title="Show Member List">${g.name}</a>
                            <#if user?? && (user.admin || g.creator==user.name)>
                                <a href="delGroup/${g.id}" title="Delete Group"><i class="icon times"></i></a>
                            </#if>
                        </td>
                        <td>${g.creator}</td>
                        <td>${g.createdTime}</td>
                        <td id="cnt_${g.id}">
                            ${g.getMemberCount()}
                        </td>
                        <#if user.inGroup(g.id)>
                        <td><button id="btn_${g.id}" onclick="joins('${g.id}')" class="ui brown button">Leave</button></td>
                        <#else>
                        <td><button id="btn_${g.id}" onclick="joins('${g.id}')" class="ui green button">Join</button></td>
                        </#if>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>

        <div class="ui modal" id="userListModal">
            <div class="header">User List</div>
            <div class="content">
                <div id="userList">

                </div>
                <div class="ui placeholder" id="placeholderList">
                    <div class="paragraph">
                        <div class="line"></div>
                        <div class="line"></div>
                        <div class="line"></div>
                        <div class="line"></div>
                        <div class="line"></div>
                    </div>
                </div>
            </div>
        </div>
    </body>

    <script>
        function showModal(id){
            $('#placeholderList').show()
            $('#userList').hide()
            $('#userListModal').modal('show')
            $.post("/userList/"+id,{},function(response){
                $('#userList').html(response)
                $('#userList').show()
                $('#placeholderList').hide()
            })
        }

        function joins(id) {
            $.toast({
                message:"Sent request... Please wait."
            })

            var btn='#btn_'+id
            var cnt='#cnt_'+id
            $.post("/join/"+id,{},function(response){
                if(response=="success_leave"){
                    $.toast({
                        class:"success",
                        message:"Left group successfully."
                    })
                    $(btn).attr('class','ui green button')
                    $(btn).text('Join')
                    $(cnt).text(parseInt($(cnt).text())-1)
                }else if(response=="success_join"){
                    $.toast({
                        class:"success",
                        message:"Joined group successfully."
                    })
                    $(btn).attr('class','ui brown button')
                    $(btn).text('Leave')
                    $(cnt).text(parseInt($(cnt).text())+1)
                }else{
                    $.toast({
                        class:"error",
                        message:response
                    })
                }
            })
        }
        $('.dropdown').dropdown();
        console.log("fuck");
    </script>
</html>