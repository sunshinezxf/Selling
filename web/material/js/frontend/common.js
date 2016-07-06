$.utf8Substr = function(srcstr,len){
	var retstr = '';	//截取后的返回字段
	var i = 0;	//当前字节位
	var n = 0.0;	//当前截取字符数
	var str_length = srcstr.length;	//字符串的字节数	
	while ((n < len) && (i <= str_length)) {	
		temp_str = srcstr.substr(i,1);
		var asc = temp_str.charCodeAt();	//得到字符串中第$i位字符的ascii码	
		if (asc >= 224){	//224－239是UTF8中文字符高位ASCII区间
			 retstr = retstr + srcstr.substr(i,1); //根据UTF-8编码规范，将3个连续的字符计为单个字符					 
			 i = i + 1;		//实际Byte计为3	
			 n = n + 2.5;	//字符数加1.5	
		}	
		else if (asc >= 192){	//如果ASCII位高与192，	
			 retstr = retstr + srcstr.substr(i,1);	//根据utf-8编码规范，将2个连续的字符计为单个字符	
			 i = i + 1;	//实际Byte计为2	
			 n = n + 2.5;	//字符数加1.5
		}
		else if (asc >= 65 && asc <= 90) {	//如果是大写字母，	
			 retstr = retstr + srcstr.substr(i,1);	
			 i = i+1;	//实际的Byte数仍计1个	
			 n = n + 2;	//但考虑整体美观，大写字母计成一个字符	
		}	
		else {	//其他情况下，包括小写字母和半角标点符号，		
			 retstr = retstr + srcstr.substr(i,1);	
			 i = i + 1;	//实际的Byte数计1个	
			 n = n + 2;	//但考虑整体美观，小写字母和半角标点计半个字符宽...	
		}	
	}	
	if (str_length > i){	
		retstr = retstr + "...";	//当实际长度超过要截取的长度时在尾处加上省略号	
	}	
	return retstr;	
}

