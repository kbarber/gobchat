proc init {argc argv} {
    variable PaneList

    ## Remove the popup, background, setup, and splash screens as they're
    ## not really part of the user-controlled install.
    set PaneList [lremove $PaneList .popup .background .setup .splash]
}

proc main {argc argv} {
    variable info
    variable PaneList

    if {[lempty $PaneList]} { exit }

    ## Hide the  . window so no one will ever find it.
    wm geometry  . 0x0+-10000+-10000
    wm title     . "$info(AppName) Setup"
    if {$::tcl_platform(platform) != "windows"} { wm override . 1 }

    SetProgramFolder

    ## If they've chosen a silent install, we just install files without ever
    ## showing any windows.
    if {$info(SilentInstall)} {
	::MPI::InstallFiles
	Exit 1
    }

    ## If they've chosen to accept all defaults, but we still want the popup,
    ## skip ahead and show the popup before installing.
    ::MPI::PaneProperty .popup EnableInDefault enabled
    if {$info(DefaultInstall) && !$enabled} {
	wm deiconify .
	update
	::MPI::Window show .copyFiles
	Exit 1
    }

    ::MPI::PaneProperty .popup Enabled enabled
    if {$enabled} { ::MPI::Window show .popup }

    wm deiconify .
    update

    if {$info(DefaultInstall)} {
	::MPI::Window show .copyFiles
	Exit 1
    }

    ::MPI::Window show .setup

    set timer 0
    ::MPI::PaneProperty .splash Enabled enabled
    if {$enabled} {
	::MPI::Window show .splash
	if {[::MPI::ImageExists .splash,Image]} {
	    ::MPI::PaneProperty .splash Timer timer
	    set timer [expr $timer * 1000]
	}
    }

    ::MPI::CreateInstallWindows [concat $PaneList .exitSetup]

    after $timer {
	::MPI::Window hide .setup
	::MPI::Window hide .splash
	::MPI::Window show .background
	::MPI::Window next
	update
    }
}

if {![info exists MPI]} { init $argc $argv; main $argc $argv }
