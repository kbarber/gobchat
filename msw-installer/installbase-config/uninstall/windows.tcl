proc ShowWindow.uninstallPopup {base} {
    ::MPI::PaneProperty $base Title title
    set title [::MPI::SubstText $title]
    set msg   [::MPI::SubstText [::MPI::GetText $base,Caption]]

    set ans [tk_messageBox -type yesno -default yes -title $title -message $msg]

    if {![info exists ::MPI]} {
    	if {$ans == "no"} { ::MPI::exit }
	::MPI::Window next
    }
}

proc ShowWindow.uninstall {base {continue 1}} {
    wm deiconify $base

    if {![info exists ::MPI]} {
    	::MPI::UninstallFiles
	if {$continue} { ::MPI::Window next }
    }

    return 1
}

proc CreateWindow.uninstall {base} {
    ###################
    # CREATING WIDGETS
    ###################
    toplevel $base
    wm withdraw  $base
    wm geometry  $base 347x110
    wm protocol  $base WM_DELETE_WINDOW exit
    wm resizable $base 0 0
    ::MPI::SetTitle    $base
    ::MPI::PlaceWindow $base -width 347 -height 110

    label $base.lab23 -bg $::bg
    ::MPI::SetImage $base.lab23 $base,Icon

    ::Progressbar::New $base.progress

    label $base.label  -bg $::bg -font $::font  -anchor sw -textvariable info(fileBeingUninstalledText)
    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 20 -y 20 -width 32 -height 32 -anchor nw -bordermode ignore 
    place $base.progress  -x 70 -y 70 -width 251 -anchor nw -bordermode ignore 
    place $base.label  -x 70 -y 15 -width 261 -height 48 -anchor nw -bordermode ignore 
}

proc ShowWindow.uninstallCompleteRemoval {base} {
    global info

    ::MPI::PaneProperty $base Title title
    set title [::MPI::SubstText $title]
    set msg   [::MPI::SubstText [::MPI::GetText $base,Caption]]

    set ans [tk_messageBox -type yesno -default yes -title $title -message $msg]

    if {![info exists ::MPI]} {
	if {$ans == "yes"} {
	    set info(Errors) [list]
	    ::MPI::UninstallFiles ::conf 1
	}

	::MPI::Window next
    }
}

proc ShowWindow.uninstallComplete {base} {
    wm deiconify  $base
    tkwait window $base
    return 1
}

proc CreateWindow.uninstallComplete {base} {
    ###################
    # CREATING WIDGETS
    ###################
    toplevel $base
    wm withdraw  $base
    wm geometry  $base 347x110
    wm protocol  $base WM_DELETE_WINDOW exit
    wm resizable $base 0 0
    ::MPI::SetTitle    $base
    ::MPI::PlaceWindow $base -width 347 -height 110

    frame $base.f
    label $base.f.icon -bg $::bg
    ::MPI::SetImage $base.f.icon $base,Icon

    label $base.f.label  -bg $::bg -font $::font  -anchor sw -anchor nw -justify left
    ::MPI::SetText $base.f.label $base,Caption

    ###################
    # SETTING GEOMETRY
    ###################
    pack $base.f -fill x
    pack $base.f.icon -side left -padx 20
    pack $base.f.label -side left

    ::MPI::NavButtons $base.buttons $base -back 0 -next 0  -cancelcmd "destroy $base"
    focus $base.buttons.cancel
}

proc ShowWindow.uninstallErrors {base} {
    wm deiconify  $base
    tkwait window $base
    return 1
}

proc CreateWindow.uninstallErrors {base} {
    ###################
    # CREATING WIDGETS
    ###################
    toplevel $base
    wm withdraw  $base
    wm geometry  $base 347x110
    wm protocol  $base WM_DELETE_WINDOW exit
    wm resizable $base 0 0
    ::MPI::SetTitle    $base
    ::MPI::PlaceWindow $base -width 347 -height 110

    frame $base.f
    label $base.f.icon -bg $::bg
    ::MPI::SetImage $base.f.icon $base,Icon

    label $base.f.label  -bg $::bg -font $::font  -anchor sw -anchor nw -justify left
    ::MPI::SetText $base.f.label $base,Caption

    ###################
    # SETTING GEOMETRY
    ###################
    pack $base.f -fill x
    pack $base.f.icon -side left -padx 20
    pack $base.f.label -side left

    ::MPI::NavButtons $base.buttons $base -back 0  -nextcmd "::MPI::Window show .uninstallDetails"  -cancelcmd "destroy $base"
    focus $base.buttons.next
}

proc ShowWindow.uninstallDetails {base} {
    wm deiconify $base
    return 1
}

proc CreateWindow.uninstallDetails {base} {
    ###################
    # CREATING WIDGETS
    ###################
    toplevel $base
    wm withdraw  $base
    wm geometry  $base 500x400
    wm protocol  $base WM_DELETE_WINDOW exit
    wm resizable $base 0 0
    if {![info exists ::MPI]} { wm transient $base .uninstallErrors }
    ::MPI::SetTitle    $base
    ::MPI::PlaceWindow $base -width 500 -height 400

    frame $base.top
    label $base.top.icon -bg $::bg
    ::MPI::SetImage $base.top.icon $base,Icon

    label $base.top.text -bg $::bg -font $::font
    ::MPI::SetText $base.top.text $base,Caption

    text $base.text -background white -font $::font
    ::MPI::SetText $base.text $base,Errors

    ###################
    # SETTING GEOMETRY
    ###################
    pack $base.top -side top -anchor w
    pack $base.top.icon -side left
    pack $base.top.text -side left -anchor sw

    pack $base.text -expand 1 -fill both

    ::MPI::NavButtons $base.buttons $base -back 0 -next 0  -cancelcmd "destroy .uninstallErrors"
}

