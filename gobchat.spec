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
rm -rf $RPM_BUILD_ROOT

mkdir -p $RPM_BUILD_ROOT/usr/share/java/gob
mkdir -p $RPM_BUILD_ROOT/var/www/html/gob
mkdir -p $RPM_BUILD_ROOT/etc/rc.d/init.d
mkdir -p $RPM_BUILD_ROOT/usr/sbin
mkdir -p $RPM_BUILD_ROOT/usr/share/doc/%{name}-${version}
mkdir -p $RPM_BUILD_ROOT/var/run/gobd
mkdir -p $RPM_BUILD_ROOT/var/log/gobd

pwd
#install -m644 $RPM_SOURCE_DIR/daemon/server.jar $RPM_BUILD_ROOT/usr/share/java/gob
#install -m755 $RPM_SOURCE_DIR/daemon/gobd $RPM_BUILD_ROOT/usr/sbin
#install -m755 $RPM_SOURCE_DIR/daemon/linux-sysv.sh $RPM_BUILD_ROOT/etc/rc.d/init.d/gobd
#install -m644 $RPM_SOURCE_DIR/website/* $RPM_BUILD_ROOT/var/www/html/gob
#install -m644 $RPM_SOURCE_DIR/INSTALL $RPM_BUILD_ROOT/usr/share/doc/${name}-${version}
#install -m644 $RPM_SOURCE_DIR/README $RPM_BUILD_ROOT/usr/share/doc/${name}-${version}
#cp -r $RPM_SOURCE_DIR/apidoc $RPM_BUILD_ROOT/usr/share/doc/${name}-${version}

%clean

%pre

%post

%preun

%postun

%files

%changelog


