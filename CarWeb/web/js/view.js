function GetCookie(Name)
{
   var search = Name + "=";
   var returnvalue = "";
   if (document.cookie.length > 0)
   {
      offset = document.cookie.indexOf(search);
   		if (offset != -1)
   		{
   			offset += search.length;
      	end = document.cookie.indexOf(";", offset);
   			if (end == -1)
      		end = document.cookie.length;
      		returnvalue=unescape(document.cookie.substring(offset,end));
    	}
    }
    return returnvalue;
}