result = MsgBox ("press yes to start the rmi service, press no to stop?", vbYesNo, "Rmi starterino")
Select Case result
Case vbYes
	Set oShell = CreateObject("WSCript.shell")
	
	oShell.run "cmd /c cd C:\Program Files\Java\jdk1.8.0_102\bin & start rmiregistry"	
	
	
	
Case vbNo
End Select

' result2 = MsgBox ("press ok to stop", vbOKOnly, "Rmi stopperino")
' Select Case result2
		' Case vbOK
		
		' oShell.quit
		' Case vbNo
' End Select