proc Toplevel {base geom args} {
    eval toplevel $base $args
    wm withdraw $base
    wm transient $base .
    wm protocol $base WM_DELETE_WINDOW Exit
    wm resizable $base 0 0
    wm geometry $base $geom
    ::MPI::SetTitle $base
    eval ::MPI::CenterWindow $base [split $geom x]
    update
}

proc SetProgramFolder {args} {
    global info

    if {[info exists ::MPI]} { return }
    if {![info exists info(ProgramFolderName)]} { return }
    if {$::tcl_platform(platform) == "windows"} {
	set folder [::MPI::WindowsDir PROGRAMS]
	set info(ProgramFolder) [file join $folder $info(ProgramFolderName)]
    } else {
	set info(ProgramFolder) $info(ProgramFolderName)
    }
}

proc SetGroupBeingInstalled {name1 name2 op} {
    global info

    set info(groupBeingInstalledText) "Installing $info(groupBeingInstalled)"
}

proc Exit {{doit 0}} {
    set base .exitSetup
    if {!$doit} {
	::MPI::PauseInstall
	::MPI::Window show $base
	tkwait variable ::quit
	if {!$::quit} {
	    grab release $base
	    catch { grab set $::conf(lastGrab) }
	    ::MPI::ContinueInstall
	    return
	}
	if {[::MPI::CancelInstall] && [info exists ::conf(unpacking)]} {
	    ::MPI::Window next
	    return
	}
    }
    ::MPI::exit
}

proc CheckExit {} {
    global info

    set program [::MPI::SubstDir $info(ProgramExecutable)]

    if {[info exists info(CreateDesktopShortcut)] \
	&& $info(CreateDesktopShortcut)} {
	if {[file exists $program]} {
	    ::MPI::CreateDesktopIcon $program $info(AppName)
	}
    }

    if {[info exists info(LaunchProgram)] && $info(LaunchProgram)} {
	if {[file exists $program]} {
	    cd $info(InstallDir)
	    exec $program &
	}
    }

    if {[info exists info(ViewReadme)] && $info(ViewReadme)} {
	::MPI::Window show .viewReadme
    }

    Exit 1
}

proc ComponentSelect {tree desc type item} {
    global ComponentInfo
    $tree selection set $item
    set comp [$tree itemcget $item -text]
    if {[winfo class $tree] == "Tree" && [$tree parent $item] != "root"} { 
	$desc configure -text ""
    } else {
	$desc configure -text $ComponentInfo($comp,desc)
    }

    if {$type == 2} {
	set c [$tree itemcget $item -window]
	$c invoke
    }
}

proc SetupTypeCheck {} {
    global info
    global SetupTypes
    global Components

    set info(SelectedFileGroups) ""
    set info(SelectedComponents) ""

    if {$info(InstallType) == "Custom"} { return }

    foreach comp $SetupTypes($info(InstallType)) {
	if {[lsearch $info(SelectedComponents) $comp] < 0} {
	    lappend info(SelectedComponents) $comp
	}
	foreach group $Components($comp) {
	    if {[lsearch $info(SelectedFileGroups) $group] < 0} {
		lappend info(SelectedFileGroups) $group
	    }
	}
    }
}

proc ComponentCheck {comp} {
    global info
    global Components
    global CustomComponents
    if {$CustomComponents($comp)} {
	set incr 1
	lappend info(SelectedComponents) $comp
    } else {
    	set incr -1
	set info(SelectedComponents) [lremove $info(SelectedComponents) $comp]
    }
    foreach group $Components($comp) { ToggleFileGroup $group $incr }
}

proc FileGroupCheck {comp group} {
    global CustomFileGroups
    set incr 1
    if {!$CustomFileGroups($comp,$group)} { set incr -1 }
    ToggleFileGroup $group $incr
}

proc ToggleFileGroup {group {incr 1}} {
    global info
    global FileGroupInfo
    global FileGroupUsage

    if {![info exists info(SpaceRequired)]} { set info(SpaceRequired) 0 }
    if {![info exists FileGroupUsage($group)]} { set FileGroupUsage($group) 0 }

    incr FileGroupUsage($group) $incr
    set size $FileGroupInfo($group,size)

    if {$FileGroupUsage($group) == 0} {
	::MPI::iincr info(SpaceRequired) -$size
	set info(SelectedFileGroups) [lremove $info(SelectedFileGroups) $group]
    } elseif {$FileGroupUsage($group) == 1 && $incr > 0} {
	::MPI::iincr info(SpaceRequired) $size
	lappend info(SelectedFileGroups) $group
    }
}

proc SetSpaceRequired {args} {
    global info
    if {$info(SpaceRequired) < 0} { set info(SpaceRequired) 0 }
    set space [expr round($info(SpaceRequired) / 1024)]K
    set info(SpaceRequiredText) "Space Required: $space"
}

proc ShowWindow.viewReadme {base} {
    variable info

    if {![winfo exists $base]} { return 0 }

    ::MPI::PaneProperties .setupComplete ReadmeFile file
    set file [::MPI::SubstDir $file]

    if {[file exists $file]} {
	set fp [open $file]
	$base.f.text insert end [read $fp]
	close $fp
    }

    wm deiconify $base
    grab $base
    tkwait window $base
    Exit 1
}

proc CreateWindow.viewReadme {base} {
    toplevel $base
    wm withdraw $base
    wm title $base [::MPI::SubstText "<AppName> Readme"]
    wm proto $base WM_DELETE_WINDOW "Exit 1"
    ::MPI::CenterWindow $base

    pack [frame $base.f] -expand 1 -fill both

    text $base.f.text -bg white -font $::font \
    	-yscrollcommand "$base.f.vs set"
    scrollbar $base.f.vs -command "$base.f.text yview"

    pack $base.f.text -side left -expand 1 -fill both
    pack $base.f.vs -side left -fill y

    ::MPI::NavButtons $base.buttons $base -back 0 -next 0 \
    	-canceltext "Finish" -cancelcmd "Exit 1"

    focus $base.f.text
}

proc ScrollLicense {args} {
    eval $args
    set w   [lindex $args 0]
    set top [winfo toplevel $w]
    lassign [$w yview] y0 y1
    if {$y1 == 1} {
	$top.buttons.next configure -state normal
    } else {
	$top.buttons.next configure -state disabled
    }
}
