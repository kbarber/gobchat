Summary: A simple java based client/server chat program
Name: gobchat
Version: 0.1
Release: 1
License: GNU
Group: Application/Internet
Source: gobchat-0.1.tar.gz
URL: http://sourceforge.net/projects/gob
BuildRoot: %{_tmppath}/%{name}-%{version}-root

%description
Gob is a simple Java based client/server chat program.

%prep
%setup

%build
ant

%install

%clean

%pre

%post

%preun

%postun

%files

%changelog


