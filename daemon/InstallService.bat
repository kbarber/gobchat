@echo off

set GobRoot="C:\docume~1\ken.barber\mydocu~1\mycode~1\gob\gobchat"

cd %GobRoot%\daemon
GobServerWin -install "Gob Online Chat Server" "c:\j2sdk1.4.2_03\jre\bin\server\jvm.dll" "-Djava.class.path=daemon\server.jar" -start sh.bob.gob.server.Main -params "config\gobd.conf.xml" -err "log\serviceerr.log" -current "%GobRoot%"