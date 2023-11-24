<#macro head>
    <head>
        <title>DDL Fighter</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta charset="UTF-8">
        <script src="https://cdn.staticfile.org/jquery/3.7.1/jquery.min.js"></script>
        <link rel="stylesheet" type="text/css" href="https://cdn.staticfile.org/fomantic-ui/2.9.2/semantic.min.css">
        <script src="https://cdn.staticfile.org/fomantic-ui/2.9.2/semantic.min.js"></script>
        <script src="https://cdn.staticfile.org/crypto-js/4.2.0/crypto-js.min.js"></script>
        <script src="static/timer.js"></script>
    </head>
</#macro>

<#macro navbar>
    <div class="ui menu sticky" style="margin-bottom: 20px; margin-top: 20px;">
        <a class="item" href="/index">
            <img class="ui image" height="30" src="/static/logo.png"
                 alt="Logo"/>
        </a>
        <a class="item" href="/index">
            <i class="clock icon"></i>
            DDLs
        </a>
        <a class="item" href="/groups">
            <i class="users icon"></i>
            Groups
        </a>

        <div class="right menu">

            <#if user??>
                <div class="ui dropdown icon item">
                    <i class="user tie icon"></i>${username}
                    <div class="menu">
                        <div class="item">
                            <a href="/logout">
                                <i class="sign out alternate icon"></i>
                                Logout
                            </a>
                        </div>
                    </div>
                </div>
            <#else>
                <a class="item" href="/login">
                    <i class="user tie icon"></i>
                    Login/Register
                </a>
            </#if>
        </div>
    </div>

    <script>
        $('.ui.sticky').sticky({ context: '#content' });
    </script>

</#macro>

<#macro footer>
    <hr/>
    <div class="ui center aligned basic segment">
        <span class="ui small grey text">
            Current Server Time: ${serverTime} <br/>
            Brought to you by XGN from HellHoleStudios 2023 <br/>
            <a href="https://stats.uptimerobot.com/JPQjxfp1w2">HHS Service Status</a> |
            <a href="https://github.com/XiaoGeNintendo/DDLFighter">Source Code</a>
        </span>
    </div>
</#macro>

<#macro ddl em forceHide=false>
    <#if em.isVisibleTo(user)==false>
        <#return>
    </#if>
    <#if em.isFinishedBy(user) && compact>
        <#return>
    </#if>
    <div class="ui fluid styled accordion" id="box_${em.internalID}" style="margin-top: 20px; margin-bottom: 20px;">
        <div class="<#if em.active(user) && !forceHide && !compact>active</#if> title">
            <h3>
                <i class="${em.getColor(user)} icon ${em.getIcon(user)}"></i>
                <span class="ui ${em.getColor(user)} text">${em.name}</span>
            </h3>
        </div>
        <div class="<#if em.active(user) && !forceHide && !compact>active</#if> content">
            <div class="ui center aligned basic segment">
                <h2 class="timedown td_${em.internalID}">Establishing Connection to the Time Master Server...</h2>
                <script>
                    registerTimer("td_${em.internalID}","${em.timeStart}","${em.timeEnd}");
                </script>
            </div>
            ${em.desc}
            <div class="ui divider"></div>
            <#if em.timeStart==em.timeEnd>
                <i class="icon calendar"></i>${em.timeStart}<br/>
            <#else>
                <i class="icon clock"></i>${em.timeStart} - ${em.timeEnd}<br/>
            </#if>

            <i class="icon user"></i>${em.uploader}<br/>
            <i class="icon calendar plus"></i>${em.addDate}<br/>
            <i class="icon dna"></i>${em.internalID}<br/>
            <i class="icon radiation"></i>${em.importance}<br/>
            <i class="icon eye"></i>${em.getVisibilityTag()}<br/>

            <#if user??>
                <#if em.getColor(user)=="green">
                    <i class="icon calendar check"></i>${user.completeDate(em)}<br/>
                </#if>
            </#if>

            <i class="icon tag"></i>
            <#list em.tag as tag>
                <div class="ui label">
                    ${tag}
                </div>
            </#list>
            <br/><br/>
            <#if user?? && (user.admin || username==em.uploader)>
                <a href="del/${em.internalID}" class="ui red button">Delete</a>
            </#if>
            <#if user?? && (user.admin || username==em.uploader)>
                <a href="edit/${em.internalID}" class="ui primary button">Edit</a>
            </#if>
            <#if user??>
                <#if em.getColor(user)=="green">
                    <a href="complete/${em.internalID}" class="ui brown button" title="When you complete this item, it will be hidden next time by default.">Did not complete</a>
                <#else>
                    <a href="complete/${em.internalID}" class="ui green button" title="When you complete this item, it will be hidden next time by default.">Complete</a>
                </#if>
            </#if>
        </div>
    </div>
</#macro>
