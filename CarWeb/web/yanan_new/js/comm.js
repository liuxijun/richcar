document.onkeydown= handleKeyPress;
function handleKeyPress(evt) {
    var nbr = (window.event)?event.keyCode:evt.which;
    if(nbr == 13) {
        var url = window.location.href;
        if('searchValue' == document.activeElement.id) {
            if(url.indexOf('searchList') > 0) {
                doSearch();
            } else {
                goToSearch();
            }

        }
    }
}