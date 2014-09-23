if "%1" == "" goto usage

rem you need to install wget.exe
wget -O /Windows/TEMP/game_%1.sgf http://www.littlegolem.net/jsp/game/png.jsp?gid=%1

start java -classpath billy.jar Billy /Windows/TEMP/game_%1.sgf

goto end
:usage
echo Usage: "ten 311455" to see game 311455
:end

