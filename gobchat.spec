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
mkdir -p $RPM_BUILD_ROOT/usr/share/doc/%{name}-%{version}
mkdir -p $RPM_BUILD_ROOT/var/run/gob
mkdir -p $RPM_BUILD_ROOT/var/log/gob
mkdir -p $RPM_BUILD_ROOT/etc/logrotate.d/

install -m644 daemon/server.jar $RPM_BUILD_ROOT/usr/share/java/gob
install -m755 daemon/gobd $RPM_BUILD_ROOT/usr/sbin
install -m755 daemon/linux-sysv.sh $RPM_BUILD_ROOT/etc/rc.d/init.d/gobd
install -m644 website/client.jar $RPM_BUILD_ROOT/var/www/html/gob
install -m644 website/index.html $RPM_BUILD_ROOT/var/www/html/gob
install -m644 INSTALL $RPM_BUILD_ROOT/usr/share/doc/%{name}-%{version}
install -m644 README $RPM_BUILD_ROOT/usr/share/doc/%{name}-%{version}
install -m644 logrotate/gob $RPM_BUILD_ROOT/etc/logrotate.d/gob
cp -r apidoc $RPM_BUILD_ROOT/usr/share/doc/%{name}-%{version}

%clean
rm -rf $RPM_BUILD_ROOT

%pre
/usr/sbin/useradd -c "Gob Online Chat User" -M -d / -r -s /bin/bash gob

%post
chown gob /var/run/gob
chgrp gob /var/run/gob
chown gob /var/log/gob
chgrp gob /var/log/gob

%preun

%postun
/usr/sbin/userdel gob


%files
%defattr(-,root,root)
%dir /var/run/gob
%dir /var/log/gob
/usr/share/java/gob
/var/www/html/gob
/usr/share/doc/%{name}-%{version}
/etc/rc.d/init.d/gobd
/usr/sbin/gobd
/etc/logrotate.d/gob

%changelog


