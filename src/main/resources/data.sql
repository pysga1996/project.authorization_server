INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('fooClientIdPassword', '$2a$10$jj4uZfGH407fDX2IC3qcwuhaT7E0hQemR0Tjs5BquUTl.NCmwClrq', 'foo,read,write',
	'password,authorization_code,refresh_token,client_credentials', null, null, 36000, 36000, null, true);
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('sampleClientId', '$2a$10$jj4uZfGH407fDX2IC3qcwuhaT7E0hQemR0Tjs5BquUTl.NCmwClrq', 'read,write,foo,bar',
	'implicit', null, null, 36000, 36000, null, false);
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('barClientIdPassword', '$2a$10$jj4uZfGH407fDX2IC3qcwuhaT7E0hQemR0Tjs5BquUTl.NCmwClrq', 'bar,read,write',
	'password,authorization_code,refresh_token', null, null, 36000, 36000, null, true);