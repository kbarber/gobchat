proc ShowWindow.popup {base} {
    ::MPI::PaneProperty $base Title title
    set title [::MPI::SubstText $title]
    set msg   [::MPI::SubstText [::MPI::GetText $base,Text]]

    set ans [tk_messageBox -type yesno -default yes -title $title -message $msg]

    if {![info exists ::MPI] && $ans == "no"} { ::MPI::exit }
}

proc ShowWindow.background {base} {
    if {![winfo exists $base]} { return 0 }
    wm deiconify $base
    return 1
}

proc CreateWindow.background {base} {
    variable images

    lassign [wm maxsize .] w h

    ::MPI::PaneProperty $base TitleBar title
    ::MPI::PaneProperty $base HideProgramManager hide

    ###################
    # CREATING WIDGETS
    ###################
    toplevel $base
    wm withdraw $base
    update idletasks
    wm transient $base .
    wm protocol $base WM_DELETE_WINDOW exit

    if {!$title} {
    	wm overrideredirect $base 1
	incr h 10
    } else {
	incr w -10
	incr h -20
    }

    if {$hide} { incr h 100 }

    wm geometry $base ${w}x${h}+0+0
    wm resizable $base 0 0
    ::MPI::SetTitle $base

    ###################
    # SETTING GEOMETRY
    ###################

    set gradient   0
    set background "white"
    ::MPI::PaneProperty $base Font font
    ::MPI::PaneProperty $base Foreground foreground
    ::MPI::PaneProperty $base Gradient1 gradient1
    ::MPI::PaneProperty $base Gradient2 gradient2
    ::MPI::PaneProperty $base GradientAxis axis
    ::MPI::PaneProperty $base Image image

    if {(![lempty $gradient1] && [lempty $gradient2])
    	|| ($gradient1 == $gradient2)} {
	## Background color only.
	set background $gradient1
    } else {
	if {![lempty $gradient1] && ![lempty $gradient2]} { set gradient 1 }
    }

    set c [canvas $base.c -background $background -bd 0 -highlightthickness 0]
    pack $c -expand 1 -fill both

    if {$gradient} {
	switch -- $axis {
	    "horizontal" { set axis x }
	    default      { set axis y }
	}
	::MPI::DrawGradient $c $axis $gradient1 $gradient2
    }

    if {![lempty $image]} {
	$c create image 40 10 -anchor nw -image $image
    } else {
	$c create text 40 10 -anchor nw  -font $font  -fill $foreground  -text [::MPI::SubstText [::MPI::GetText $base,Text]]
    }

    update idletasks

    return 1
}

proc ShowWindow.setup {base} {
    if {![winfo exists $base]} { return 0 }
    wm deiconify $base
    return 1
}

proc CreateWindow.setup {base} {
    ###################
    # CREATING WIDGETS
    ###################
    toplevel $base
    wm withdraw $base
    update idletasks
    wm transient $base .
    wm protocol $base WM_DELETE_WINDOW exit
    wm focusmodel $base passive
    wm geometry $base 347x110
    wm overrideredirect $base 0
    wm resizable $base 0 0
    ::MPI::SetTitle $base
    ::MPI::PlaceWindow $base -width 347 -height 110

    label $base.lab23 -bg $::bg
    ::MPI::SetImage $base.lab23 $base,Icon

    ::Progressbar::New $base.progress

    text $base.tex25  -bg $::bg -font $::font  -height 48 -relief flat -state disabled -width 261  -wrap word -borderwidth 0 -highlightthickness 0
    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 20 -y 20 -width 32 -height 32 -anchor nw -bordermode ignore 
    place $base.progress  -x 70 -y 70 -width 251 -anchor nw -bordermode ignore 
    place $base.tex25  -x 70 -y 15 -width 261 -height 48 -anchor nw -bordermode ignore 

    ::MPI::SetText $base.tex25 $base,Caption
}

proc ShowWindow.splash {base} {
    if {![winfo exists $base]} { return 0 }
    wm deiconify $base
    return 1
}

proc CreateWindow.splash {base} {
    if {![::MPI::PaneProperty $base Timer timer]} { return }
    if {![::MPI::ImageExists $base,Image]} { return }

    set image  [::MPI::GetImage $base,Image]
    set width  [image width $image]
    set height [image height $image]

    toplevel    $base
    wm withdraw $base
    wm override $base 1
    ::MPI::PlaceWindow $base -width $width -height $height

    label $base.l -bg $::bg
    ::MPI::SetImage $base.l $base,Image
    pack $base.l

    bind $base.l <Button-1> "::MPI::Window hide $base"
}

proc ShowWindow.welcome {base} {
    wm deiconify $base
    return 1
}

proc CreateWindow.welcome {base} {
    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 473x335

    label $base.lab23 -bg $::bg -height 0  -relief sunken -width 0
    ::MPI::SetImage $base.lab23 $base,Image

    frame $base.fra29  -borderwidth 2 -height 2 -relief groove -width 440

    label $base.lab30 -bg $::bg -height 0 -width 0
    ::MPI::SetImage $base.lab30 $base,Icon

    text $base.tex32  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 203  -relief flat  -selectbackground #000080800000 -state disabled -width 296 -wrap word 
    text $base.tex51  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 33  -relief flat -state disabled  -width 261 -wrap word 
    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 15 -y 15 -anchor nw -bordermode ignore 
    place $base.fra29  -x 15 -y 285 -width 440 -height 2 -anchor nw -bordermode ignore 
    place $base.lab30  -x 155 -y 20 -width 32 -height 32 -anchor nw -bordermode ignore 
    place $base.tex32  -x 155 -y 70 -width 296 -height 203 -anchor nw -bordermode ignore 
    place $base.tex51  -x 200 -y 20 -width 261 -height 45 -anchor nw -bordermode ignore 

    ::MPI::NavButtons $base.buttons $base

    ::MPI::CenterWindow $base

    ::MPI::SetText $base.tex51 $base,Caption
    ::MPI::SetText $base.tex32 $base,Text
}

proc ShowWindow.license {base} {
    set t $base.fra36.tex39

    ::MPI::PaneProperty $base ForceYes  forceyes
    ::MPI::PaneProperty $base ForceRead forceread

    ## We need to deiconify and update so we can find out if the text in
    ## the text box is longer than displayed area.
    wm deiconify $base
    update

    ## If we're forcing the user to read to the bottom, and the license
    ## actually extends beyond the scrollable area, re-configure the
    ## scrollbar to configure the next button.
    if {$forceread && [$t yview] != "0 1"} {
        $base.fra36.scr40 configure -command "ScrollLicense $t yview"
	$base.buttons.next configure -state disabled
    }

    if {$forceyes} { $base.buttons.next configure -state disabled }

    return 1
}

proc CreateWindow.license {base} {
    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 473x335

    ::MPI::PaneProperty $base ForceYes  forceyes
    ::MPI::PaneProperty $base ForceRead forceread

    label $base.lab34 -bg $::bg -height 0 -width 0 
    ::MPI::SetImage $base.lab34 $base,Icon

    frame $base.fra36  -borderwidth 2 -height 191 -relief sunken -width 444 
    set t [text $base.fra36.tex39  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -relief flat -state disabled  -width 28 -yscrollcommand "$base.fra36.scr40 set"]
    ::MPI::SetText $base.fra36.tex39 $base,Info

    scrollbar $base.fra36.scr40 -command "$t yview" -width 14 

    text $base.tex52  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 33  -relief flat -state disabled  -width 401 -wrap word 
    ::MPI::SetText $base.tex52 $base,Caption

    text $base.tex22  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 33  -relief flat -state disabled  -width 446 -wrap word 
    ::MPI::SetText $base.tex22 $base,Text
    place $base.tex22 -x 17 -y 255 -width 446 -height 33 -anchor nw

    if {$forceyes} {
	checkbutton $base.accept  -bg $::bg -font $::font  -variable ::info(AcceptLicense) -command "
		if {\$::info(AcceptLicense)} {
		    $base.buttons.next configure -state normal
		} else {
		    $base.buttons.next configure -state disabled
		}
	    "
	::MPI::SetText $base.accept $base,AcceptCheck
	place $base.accept -x 17 -y 250 -anchor nw
	place $base.tex22 -x 20 -y 270 -width 446 -height 33 -anchor nw
    }

    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab34  -x 20 -y 10 -width 32 -height 32 -anchor nw -bordermode ignore 
    place $base.fra36  -x 20 -y 55 -width 444 -height 191 -anchor nw -bordermode ignore 
    grid columnconf $base.fra36 0 -weight 1
    grid rowconf    $base.fra36 0 -weight 1
    grid $base.fra36.tex39  -in $base.fra36 -column 0 -row 0 -columnspan 1 -rowspan 1  -sticky nesw 
    grid $base.fra36.scr40  -in $base.fra36 -column 1 -row 0 -columnspan 1 -rowspan 1 -sticky ns 
    place $base.tex52  -x 60 -y 10 -width 401 -height 33 -anchor nw -bordermode ignore 

    bind $base <MouseWheel> "::MPI::WheelScroll $base.fra36.tex39 %D"

    ::MPI::NavButtons $base.buttons $base

    ::MPI::CenterWindow $base
}

proc ShowWindow.readme {base} {
    wm deiconify $base
    return 1
}

proc CreateWindow.readme {base} {
    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 473x335

    label $base.lab23 -bg $::bg -height 0 -relief sunken -width 0 
    ::MPI::SetImage $base.lab23 $base,Image

    frame $base.fra29  -borderwidth 2 -height 2 -relief groove -width 440 
    frame $base.fra22  -borderwidth 2 -height 241 -relief sunken -width 305 
    text $base.fra22.tex23  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 5  -relief flat -state disabled  -width 18 -wrap word  -yscrollcommand "$base.fra22.scr24 set"  -xscrollcommand "$base.fra22.scr25 set"
    scrollbar $base.fra22.scr24  -command "$base.fra22.tex23 yview" -width 14 
    scrollbar $base.fra22.scr25  -command "$base.fra22.tex23 xview" -orient horizontal -width 14 
    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 15 -y 15 -anchor nw -bordermode ignore 
    place $base.fra29  -x 15 -y 285 -width 440 -height 2 -anchor nw -bordermode ignore 
    place $base.fra22  -x 150 -y 35 -width 305 -height 241 -anchor nw -bordermode ignore 
    grid columnconf $base.fra22 0 -weight 1
    grid rowconf $base.fra22 0 -weight 1
    grid $base.fra22.tex23  -in $base.fra22 -column 0 -row 0 -columnspan 1 -rowspan 1  -sticky nesw 
    grid $base.fra22.scr24  -in $base.fra22 -column 1 -row 0 -columnspan 1 -rowspan 1 -sticky ns 
    grid $base.fra22.scr25  -in $base.fra22 -column 0 -row 2 -columnspan 1 -rowspan 1 -sticky ew 

    bind $base <MouseWheel> "::MPI::WheelScroll $base.fra22.tex23 %D"

    ::MPI::NavButtons $base.buttons $base

    ::MPI::CenterWindow $base

    ::MPI::SetText $base.fra22.tex23 $base,Info
}

proc ShowWindow.userInfo {base} {
    wm deiconify $base
    return 1
}

proc CreateWindow.userInfo {base} {
    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 473x335

    label $base.lab23 -bg $::bg -height 0 -relief sunken -width 0 
    ::MPI::SetImage $base.lab23 $base,Image

    frame $base.fra29  -borderwidth 2 -height 2 -relief groove -width 440 
    text $base.tex28  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 33  -relief flat -state disabled  -width 311 -wrap word 
    entry $base.ent29 -textvariable info(UserName)  -width 231 -background white -font $::font
    entry $base.ent30 -textvariable info(UserCompany)  -width 231 -background white -font $::font
    entry $base.ent31 -textvariable info(UserSerial)  -width 231 -background white -font $::font
    label $base.lab32  -bg $::bg -font $::font -text Name: 
    label $base.lab33  -bg $::bg -font $::font -text Company: 
    label $base.lab34  -bg $::bg -font $::font -text Serial: 
    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 15 -y 15 -anchor nw -bordermode ignore 
    place $base.fra29  -x 15 -y 285 -width 440 -height 2 -anchor nw -bordermode ignore 
    place $base.tex28  -x 150 -y 15 -width 311 -height 33 -anchor nw -bordermode ignore 
    place $base.ent29  -x 225 -y 105 -width 231 -height 19 -anchor nw -bordermode ignore 
    place $base.ent30  -x 225 -y 140 -width 231 -height 19 -anchor nw -bordermode ignore 
    place $base.ent31  -x 225 -y 175 -width 231 -height 19 -anchor nw -bordermode ignore 
    place $base.lab32  -x 150 -y 105 -anchor nw -bordermode ignore 
    place $base.lab33  -x 150 -y 140 -anchor nw -bordermode ignore 
    place $base.lab34  -x 150 -y 175 -anchor nw -bordermode ignore 

    ::MPI::NavButtons $base.buttons $base

    ::MPI::CenterWindow $base

    ::MPI::SetText $base.tex28 $base,Caption
    focus $base.ent29
}

proc PreviewWindow.destLoc {base} {
    variable info
    set info(InstallDir) $::info([Platform],InstallDir)
}

proc ShowWindow.destLoc {base} {
    wm deiconify $base
    return 1
}

proc CreateWindow.destLoc {base} {
    variable info

    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 473x335

    label $base.lab23 -bg $::bg -height 0 -relief sunken -width 0 
    ::MPI::SetImage $base.lab23 $base,Image

    frame $base.fra29  -borderwidth 2 -height 2 -relief groove -width 440 
    text $base.tex28  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 193  -relief flat -state disabled  -width 311 -wrap word 
    frame $base.fra35  -borderwidth 2 -height 40 -relief groove -width 305 
    label $base.fra35.lab38  -bg $::bg -font $::font  -height 0 -width 0 -textvariable info(InstallDir) -anchor w
    label $base.lab36  -bg $::bg -font $::font -text "Destination Folder"
    ::MPI::button $base.but37  -bg $::bg -font $::font  -pady 0 -text "Browse..."  -command  "::MPI::SetInstallDir \[tk_chooseDirectory  -parent $base -initialdir [list $info(InstallDir)]]"
    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 15 -y 15 -anchor nw -bordermode ignore 
    place $base.fra29  -x 15 -y 285 -width 440 -height 2 -anchor nw -bordermode ignore 
    place $base.tex28  -x 150 -y 15 -width 311 -height 193 -anchor nw -bordermode ignore 
    place $base.fra35  -x 150 -y 235 -width 305 -height 40 -anchor nw -bordermode ignore 
    place $base.fra35.lab38  -x 5 -y 13 -width 210 -anchor nw -bordermode ignore 
    place $base.lab36  -x 158 -y 225 -anchor nw -bordermode ignore 
    place $base.but37  -x 370 -y 245 -width 74 -height 22 -anchor nw -bordermode ignore 

    ::MPI::NavButtons $base.buttons $base -nextcmd  "if {\[::MPI::CheckInstallDir] == 1} { ::MPI::Window next $base }"

    ::MPI::CenterWindow $base

    ::MPI::SetText $base.tex28 $base,Caption
    ::MPI::SetText $base.but37 $base,BrowseButton
}

proc PreviewWindow.setupType {base} {
    variable info
    variable SetupTypes
    variable SetupTypeInfo

    set info(SetupTypes) $info(SetupTypes,[Platform])

    foreach type $info(SetupTypes) { set SetupTypes($type) 0 }

    upvar #0 [Platform]_SetupTypeInfo tmp

    array set SetupTypeInfo [array get tmp]
}

proc ShowWindow.setupType {base} {
    wm deiconify $base
    return 1
}

proc CreateWindow.setupType {base} {
    variable info
    variable SetupTypes
    variable SetupTypeInfo

    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 473x335

    label $base.lab23 -bg $::bg -height 0 -relief sunken -width 0 
    ::MPI::SetImage $base.lab23 $base,Image

    text $base.tex28  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 33  -relief flat -state disabled  -width 311 -wrap word 

    frame $base.fra29  -borderwidth 2 -height 2 -relief groove -width 440 

    set f [frame $base.types -width 297]

    set i 0
    set row -1
    set numTypes [llength [array names SetupTypes]]
    if {$numTypes > 0} {
	set pad [expr 30 / $numTypes]
	foreach type $info(SetupTypes) {
	    if {![info exists SetupTypes($type)]} { continue }
	    set rad [radiobutton $f.rad[incr i]  -bg $::bg -font $::font  -justify right -text $type -underline 0  -value $type -variable info(InstallType)  -command SetupTypeCheck  -highlightthickness 0 -anchor w  ]
	    set desc $SetupTypeInfo($type,desc)
	    set lab [label $f.lab$i -text $desc  -bg $::bg -font $::font  -anchor w -wraplength 230 -justify left  ]

	    grid $rad -row [incr row] -column 0 -sticky w
	    grid $lab -row $row -column 1 -sticky w -ipady $pad
	}
    }

    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 15 -y 15 -anchor nw -bordermode ignore 

    place $base.fra29  -x 15 -y 285 -width 440 -height 2 -anchor nw -bordermode ignore 
    place $base.tex28  -x 150 -y 15 -width 311 -height 33 -anchor nw -bordermode ignore 

    place $f  -x 145 -y 65 -anchor nw -bordermode ignore

    ::MPI::NavButtons $base.buttons $base

    ::MPI::CenterWindow $base

    ::MPI::SetText $base.tex28 $base,Caption
}

proc PreviewWindow.customSetup {base} {
    variable info

    set info(InstallType) Custom
}

proc ShowWindow.customSetup {base} {
    variable info

    if {$info(InstallType) != "Custom"} { return 0 }
    wm deiconify $base
    return 1
}

proc CreateWindow.customSetup {base} {
    variable info
    variable Components
    variable SetupTypes

    ## Whenever SpaceRequired is modified, we want to set
    ## SpaceRequiredText as a result.
    ::MPI::Trace ::info(SpaceRequired) SetSpaceRequired

    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 473x335

    label $base.lab23 -bg $::bg -height 0 -relief sunken -width 0 
    ::MPI::SetImage $base.lab23 $base,Image

    frame $base.fra29  -borderwidth 2 -height 2 -relief groove -width 440 
    text $base.tex28  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 33  -relief flat -state disabled  -width 311 -wrap word 
    label $base.lab47  -bg $::bg -font $::font  -height 0 -text "Components" -underline 0 -width 0 

    set cmd ListBox
    ::MPI::PaneProperty $base ShowFileGroups showFileGroups
    if {$showFileGroups} { set cmd Tree }
    set list [$cmd $base.list -height 91 -width 301  -background white -font $::font  -yscrollcommand "$base.vsc set" -deltay 20 -padx 27]
    scrollbar $base.vsc -command "$base.list yview"

    frame $base.fra24  -borderwidth 2 -height 75 -relief groove -width 300 
    set msg [message $base.fra24.desc -anchor nw -width 290  -bg $::bg -font $::font]
    label $base.lab26  -bg $::bg -font $::font  -height 0 -text "Description" -width 0 
    label $base.lab27 -height 0 -width 148  -bg $::bg -font $::font  -text "Space Required: 0K" -textvariable info(SpaceRequiredText)
    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 15 -y 15 -anchor nw -bordermode ignore 
    place $base.fra29  -x 15 -y 285 -width 440 -height 2 -anchor nw -bordermode ignore 
    place $base.tex28  -x 150 -y 15 -width 311 -height 33 -anchor nw -bordermode ignore 
    place $base.lab47  -x 148 -y 55 -anchor nw -bordermode ignore 
    place $base.list  -x 150 -y 75 -width 285 -height 91 -anchor nw -bordermode ignore 
    place $base.vsc  -x 434 -y 75 -width 16 -height 91 -anchor nw -bordermode ignore 
    place $base.fra24  -x 150 -y 180 -width 300 -height 75 -anchor nw -bordermode ignore 
    place $base.fra24.desc  -x 3 -y 5 -width 290 -height 66 -anchor nw -bordermode ignore 
    place $base.lab26  -x 157 -y 170 -anchor nw -bordermode ignore 
    place $base.lab27  -x 145 -y 260 -width 148 -height 19 -anchor nw -bordermode ignore 

    bind $base <MouseWheel> "::MPI::WheelScroll $base.list %D"

    ::MPI::NavButtons $base.buttons $base

    ::MPI::CenterWindow $base

    ::MPI::SetText $base.tex28 $base,Caption

    $list bindText <Button-1> "ComponentSelect $list $msg 1"
    $list bindText <Double-1> "ComponentSelect $list $msg 2"

    if {![info exists SetupTypes(Custom)]} { return }

    set i 0
    foreach comp $SetupTypes(Custom) {
	if {![info exists Components($comp)]} { continue }
	set c [checkbutton $list.[incr i] -bg white -font $::font  -padx 0 -pady 0 -bd 1 -highlightthickness 0  -variable CustomComponents($comp)  -command "ComponentCheck [list $comp]"]
	if {$showFileGroups} {
	    $list insert end root $i -text $comp -window $c

	    set compnode $i
	    foreach group $Components($comp) {
		incr i
		set c [checkbutton $list.$i -bg white -font $::font  -padx 0 -pady 0 -bd 1 -highlightthickness 0  -variable CustomFileGroups($comp,$group)  -command  "FileGroupCheck [list $comp] [list $group]"]
		$list insert end $compnode $i -text $group -window $c
	    }
	} else {
	    $list insert end $i -text $comp -window $c
	}

	incr i
    }
}

proc ShowWindow.selectFolder {base} {
    wm deiconify $base
    return 1
}

proc CreateWindow.selectFolder {base} {
    global info

    ## Whenever ProgramFolderName is modified, we want to set
    ## ProgramFolder as a result.
    ::MPI::Trace ::info(ProgramFolderName) SetProgramFolder

    ::MPI::PaneProperty $base DefaultFolder folder
    set info(ProgramFolderName) [::MPI::SubstDir $folder]

    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 473x335

    label $base.lab23 -bg $::bg -height 0 -relief sunken -width 0 
    ::MPI::SetImage $base.lab23 $base,Image

    frame $base.fra29  -borderwidth 2 -height 2 -relief groove -width 440 
    text $base.tex28  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 48  -relief flat -state disabled  -width 311 -wrap word 
    label $base.lab30  -bg $::bg -font $::font  -text "Program Folders:" -underline 0 
    entry $base.ent31  -bg white -font $::font  -width 301 -textvariable info(ProgramFolderName)
    label $base.lab32  -bg $::bg -font $::font  -text "Existing Folders:" -underline 1 
    listbox $base.lis33  -background white -font $::font  -height 111 -width 301 -selectmode single  -yscrollcommand "$base.vsc set"
    scrollbar $base.vsc -command "$base.lis33 yview"

    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 15 -y 15 -anchor nw -bordermode ignore 
    place $base.fra29  -x 15 -y 285 -width 440 -height 2 -anchor nw -bordermode ignore 
    place $base.tex28  -x 150 -y 15 -width 311 -height 48 -anchor nw -bordermode ignore 
    place $base.lab30  -x 150 -y 70 -anchor nw -bordermode ignore 
    place $base.ent31  -x 152 -y 90 -width 301 -height 19 -anchor nw -bordermode ignore 
    place $base.lab32  -x 150 -y 120 -anchor nw -bordermode ignore 
    place $base.lis33  -x 152 -y 140 -width 285 -height 111 -anchor nw -bordermode ignore 
    place $base.vsc  -x 436 -y 140 -width 16 -height 111 -anchor nw -bordermode ignore 

    ::MPI::NavButtons $base.buttons $base

    ::MPI::CenterWindow $base

    ::MPI::SetText $base.tex28 $base,Caption

    if {![info exists ::MPI]} {
	set folders [list]
	set dir [::MPI::WindowsDir PROGRAMS]
	foreach f [glob -nocomplain -type d [file join $dir *]] {
	    lappend folders [file tail $f]
	}
	eval $base.lis33 insert end [lsort -dict $folders]
    }

    bind $base.lis33 <Double-1> "
	set info(ProgramFolderName) \[$base.lis33 get \[$base.lis33 cursel]]
    "
    bind $base <MouseWheel> "::MPI::ListScroll $base.lis33 %D"
}

proc ShowWindow.startCopy {base} {
    ::MPI::SetText $base.fra36.tex37 $base,Text
    wm deiconify $base
    return 1
}

proc CreateWindow.startCopy {base} {
    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 473x335

    label $base.lab23 -bg $::bg -height 0 -relief sunken -width 0 
    ::MPI::SetImage $base.lab23 $base,Image

    frame $base.fra29  -borderwidth 2 -height 2 -relief groove -width 440 
    text $base.tex28  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 48  -relief flat -state disabled  -width 311 -wrap word 
    label $base.lab34  -bg $::bg -font $::font  -height 0 -text "Current Settings:" -width 0 
    frame $base.fra36  -borderwidth 2 -height 165 -relief sunken -width 305 
    text $base.fra36.tex37  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 1  -relief flat -state disabled  -width 6 -wrap none  -yscrollcommand "$base.fra36.scr38 set"  -xscrollcommand "$base.fra36.scr39 set"
    scrollbar $base.fra36.scr38  -command "$base.fra36.tex37 yview" -width 13 
    scrollbar $base.fra36.scr39  -command "$base.fra36.tex37 xview" -orient horizontal -width 13 
    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 15 -y 15 -anchor nw -bordermode ignore 
    place $base.fra29  -x 15 -y 285 -width 440 -height 2 -anchor nw -bordermode ignore 
    place $base.tex28  -x 150 -y 15 -width 311 -height 48 -anchor nw -bordermode ignore 
    place $base.lab34  -x 150 -y 90 -anchor nw -bordermode ignore 
    place $base.fra36  -x 153 -y 110 -width 305 -height 165 -anchor nw -bordermode ignore 
    grid columnconf $base.fra36 0 -weight 1
    grid rowconf $base.fra36 0 -weight 1
    grid $base.fra36.tex37  -in $base.fra36 -column 0 -row 0 -columnspan 1 -rowspan 1  -sticky nesw 
    grid $base.fra36.scr38  -in $base.fra36 -column 1 -row 0 -columnspan 1 -rowspan 1 -sticky ns 
    grid $base.fra36.scr39  -in $base.fra36 -column 0 -row 2 -columnspan 1 -rowspan 1 -sticky ew 

    bind $base <MouseWheel> "::MPI::WheelScroll $base.fra36.tex37 %D"

    ::MPI::NavButtons $base.buttons $base

    ::MPI::CenterWindow $base

    ::MPI::SetText $base.tex28 $base,Caption
    ::MPI::SetText $base.fra36.tex37 $base,Text
}

proc PreviewWindow.copyFiles {base} {
    set ::info(groupBeingInstalledText) "Installing Program Files..."
    set ::info(fileBeingInstalled) "some file.txt"
}

proc ShowWindow.copyFiles {base} {
    variable info

    wm deiconify $base
    if {[info exists ::MPI]} { return 1 }
    if {[winfo exists $base.cancel]} { focus $base.cancel }
    update
    ::MPI::InstallFiles
    if {!$info(DefaultInstall)} { ::MPI::Window next $base }
    return 1
}

proc CreateWindow.copyFiles {base} {
    ###################
    # CREATING WIDGETS
    ###################
    toplevel $base
    wm transient $base .
    wm withdraw $base
    ::MPI::PaneProperty $base AllowCancel allowCancel
    if {$allowCancel} {
	wm protocol $base WM_DELETE_WINDOW exit
    } else {
	wm protocol $base WM_DELETE_WINDOW noop
    }
    wm focusmodel $base passive
    wm geometry $base 347x110
    wm resizable $base 0 0
    ::MPI::SetTitle $base

    ::MPI::PlaceWindow $base -width 347 -height 110

    label $base.lab23 -bg $::bg
    ::MPI::SetImage $base.lab23 $base,Icon

    set pbarWidth 250
    if {$allowCancel} {
    	set pbarWidth 200

	button $base.cancel  -bg $::bg -font $::font  -text "Cancel" -command "Exit"

	::MPI::SetText $base.cancel $base,CancelButton
    }

    ::Progressbar::New $base.progress -width $pbarWidth

    ::MPI::Trace ::info(groupBeingInstalled) SetGroupBeingInstalled

    label $base.copy  -bg $::bg -font $::font  -textvariable info(groupBeingInstalledText)
    label $base.label  -bg $::bg -font $::font  -textvariable info(fileBeingInstalled)
    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 20 -y 20 -width 32 -height 32 -anchor nw -bordermode ignore 
    place $base.progress  -x 70 -y 70 -width $pbarWidth -anchor nw -bordermode ignore 
    place $base.copy  -x 70 -y 15 -anchor nw -bordermode ignore 
    place $base.label  -x 70 -y 50 -anchor nw -bordermode ignore

    if {$allowCancel} {
	place $base.cancel  -x 272 -y 70 -width 74 -height 22 -anchor nw -bordermode ignore
    }

    ::MPI::SetText $base.copy $base,Caption
}

proc ShowWindow.setupComplete {base} {
    wm deiconify $base
    focus $base.buttons.cancel
    return 1
}

proc CreateWindow.setupComplete {base} {
    variable info

    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 473x335

    label $base.lab23 -bg $::bg -height 0 -relief sunken -width 0 
    ::MPI::SetImage $base.lab23 $base,Image

    frame $base.fra29  -borderwidth 2 -height 2 -relief groove -width 440 
    text $base.tex28  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 73  -relief flat -state disabled  -width 306 -wrap word 

    set i 25
    set y 100
    ::MPI::PaneProperty $base IncludeReadme,Condition cond
    ::MPI::PaneProperty $base IncludeReadme value
    if {[::MPI::EvalCondition $cond] && $value} {
	checkbutton $base.che41 -variable info(ViewReadme)  -bg $::bg -font $::font -highlightthickness 0
	place $base.che41 -x 145 -y $y -anchor nw -bordermode ignore 
	::MPI::SetText $base.che41 $base,ReadmeButton
	incr y $i
    }

    ::MPI::PaneProperty $base IncludeLaunch,Condition cond
    ::MPI::PaneProperty $base IncludeLaunch value
    if {[::MPI::EvalCondition $cond] && $value} {
	checkbutton $base.che42 -variable info(LaunchProgram)  -bg $::bg -font $::font -highlightthickness 0
	place $base.che42 -x 145 -y $y -anchor nw -bordermode ignore 
	::MPI::SetText $base.che42 $base,LaunchButton
    	incr y $i	
    }

    ::MPI::PaneProperty $base IncludeDesktop,Condition cond
    ::MPI::PaneProperty $base IncludeDesktop value
    if {[::MPI::EvalCondition $cond] && $value} {
	checkbutton $base.che43 -variable info(CreateDesktopShortcut)  -bg $::bg -font $::font -highlightthickness 0
	place $base.che43 -x 145 -y $y -anchor nw -bordermode ignore 
	::MPI::SetText $base.che43 $base,DesktopButton
    	incr y $i	
    }

    text $base.tex43  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -height 73  -relief flat -state disabled  -width 306 -wrap word 
    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 15 -y 15 -anchor nw -bordermode ignore 
    place $base.fra29  -x 15 -y 285 -width 440 -height 2 -anchor nw -bordermode ignore 
    place $base.tex28  -x 150 -y 15 -width 306 -height 73 -anchor nw -bordermode ignore 
    place $base.tex43  -x 150 -y 210 -width 306 -height 73 -anchor nw -bordermode ignore 

    ::MPI::NavButtons $base.buttons $base -cancelcmd "CheckExit"
    focus $base.buttons.cancel

    ::MPI::CenterWindow $base

    ::MPI::SetText $base.tex28 $base,Caption
    ::MPI::SetText $base.tex43 $base,Text
}

proc ShowWindow.exitSetup {base} {
    wm deiconify $base
    focus $base.but25
    return 1
}

proc CreateWindow.exitSetup {base} {
    ###################
    # CREATING WIDGETS
    ###################
    Toplevel $base 327x156

    label $base.lab23 -bg $::bg
    ::MPI::SetImage $base.lab23 $base,Icon

    text $base.tex24  -bg $::bg -font $::font  -borderwidth 0 -highlightthickness 0  -relief flat  -state disabled -width 251 -wrap word
       
    ::MPI::button $base.but25  -bg $::bg -font $::font  -height 21 -pady 0 -text Yes -underline 0 -width 45  -command "set quit 0; ::MPI::Window hide $base"
    ::MPI::button $base.but26  -bg $::bg -font $::font  -pady 0 -text No -underline 0  -command "set quit 1; ::MPI::Window hide $base"

    ::MPI::SetText $base.tex24 $base,Caption
    ::MPI::SetText $base.but25 $base,YesButton
    ::MPI::SetText $base.but26 $base,NoButton

    ###################
    # SETTING GEOMETRY
    ###################
    place $base.lab23  -x 15 -y 15 -width 32 -height 32 -anchor nw -bordermode ignore 
    place $base.tex24  -x 65 -y 15 -width 251 -height 120 -anchor nw -bordermode ignore 
    place $base.but25  -x 105 -y 127 -width 74 -height 22 -anchor nw -bordermode ignore 
    place $base.but26  -x 185 -y 127 -width 74 -height 22 -anchor nw -bordermode ignore 

    ::MPI::CenterWindow $base 327 156

    focus $base.but25

    bind $base.but25 <Key-Return> "$base.but25 invoke"
    bind $base.but26 <Key-Return> "$base.but26 invoke"
}

