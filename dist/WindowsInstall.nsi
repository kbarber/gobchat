; WindowsInstall.nsi
!verbose 4

;--------------------------------

; The name of the installer
Name "Gob Online Chat Installer"

Var GOBROOT

; The file to write
OutFile "Install Gob.exe"

; The default installation directory
InstallDir "$PROGRAMFILES\Gob Online Chat"

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\Gob Online Chat" "Install_Dir"

;--------------------------------

; Pages

Page components
Page directory
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

;--------------------------------

; The stuff to install
Section "Gob Online Chat (required)"

  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Put file there
  File "..\config\gobd.conf.xml"
  File "..\daemon\GobServerWin.exe"
  File "..\daemon\server.jar"
  File "..\doc\LICENSE"
  File "..\doc\LICENSE.JavaService.txt"
  File "..\doc\README"
  File "..\doc\CHANGELOG"

  Call GetWebRoot
  Pop $R0

  CreateDirectory $R0\gob
  SetOutPath $R0\gob
  File "..\website\index.html"
  File "..\website\gobapplet.html"
  File "..\website\client.jar"

  CreateDirectory $INSTDIR\log

  ;File /r "..\daemon"
  ;File /r "..\config"
  ;File /r "..\doc"
  ;File /r "..\log"
  ;File /r "..\website"
  
  ; Write the installation path into the registry
  WriteRegStr HKLM SOFTWARE\GobOnlineChat "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat" "InstallLocation" "%INSTDIR"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat" "Publisher" "Ken Barber"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat" "URLInfoAbout" "http://gob.sourceforge.net/"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat" "DisplayVersion" "0.4"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat" "DisplayName" "Gob Online Chat"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat" "VersionMajor" 0
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat" "VersionMinor" 4
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat" "NoRepair" 1
  WriteUninstaller "uninstall.exe"

  ; Register service

  Call GetJRE
  Pop $R0
  
  StrCpy $GOBROOT "c:\progra~1\gobonl~1"
 
  SetOutPath '$GOBROOT'
  ExecWait 'GobServerWin -install "Gob Online Chat Server" $R0 -Djava.class.path=server.jar -start sh.bob.gob.server.Main -params gobd.conf.xml -err log\serviceerr.log -current $GOBROOT'
  Exec 'sc start "Gob Online Chat Server"'
  
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"

  CreateDirectory "$SMPROGRAMS\Gob Online Chat"
  CreateShortCut "$SMPROGRAMS\Gob Online Chat\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  SetOutPath "$SMPROGRAMS\Gob Online Chat"
  File "../dist/Gob Online Chat.url"
  ; CreateShortCut "$SMPROGRAMS\Example2\Example2 (MakeNSISW).lnk" "$INSTDIR\makensisw.exe" "" "$INSTDIR\makensisw.exe" 0
  
SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\GobOnlineChat"
  DeleteRegKey HKLM SOFTWARE\GobOnlineChat

  ; Deregister service
  StrCpy $GOBROOT "c:\progra~1\gobonl~1"
  
  SetOutPath $GOBROOT
  ExecWait 'sc stop "Gob Online Chat Server"'
  ExecWait 'GobServerWin -uninstall "Gob Online Chat Server"'

  ; Remove shortcuts, if any
  Delete "$SMPROGRAMS\Gob Online Chat\*.*"

  ; Remove IIS website
  Call un.GetWebRoot
  Pop $R0

  RMDir /r $R0\gob

  ; Remove directories used
  RMDir "$SMPROGRAMS\Gob Online Chat"

  ; Remove files and uninstaller
  RMDir /r $INSTDIR
  RMDir $INSTDIR

SectionEnd

Function GetJRE
;
;  Find JRE (Java.exe)
;  1 - in .\jre directory (JRE Installed with application)
;  2 - in JAVA_HOME environment variable
;  3 - in the registry
;  4 - assume java.exe in current dir or PATH

  Push $R0
  Push $R1

  ClearErrors
  StrCpy $R0 "$EXEDIR\jre\bin\server\jvm.dll"
  IfFileExists $R0 JreFound
  StrCpy $R0 ""

  ClearErrors
  ReadEnvStr $R0 "JAVA_HOME"
  StrCpy $R0 "$R0\bin\server\jvm.dll"
  IfErrors 0 JreFound

  ClearErrors
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$R1" "JavaHome"
  StrCpy $R0 "$R0\jre\bin\server\jvm.dll"

  IfErrors 0 JreFound
  StrCpy $R0 "jvm.dll"
        
 JreFound:
  Pop $R1
  Exch $R0
FunctionEnd

Function GetWebRoot
    Push $R0

    ReadRegStr $R0 HKLM "SOFTWARE\Microsoft\InetStp" "PathWWWRoot"
    Exch $R0
FunctionEnd

Function un.GetWebRoot
    Push $R0

    ReadRegStr $R0 HKLM "SOFTWARE\Microsoft\InetStp" "PathWWWRoot"
    Exch $R0
FunctionEnd
