SELECT setval('company_sequence', (SELECT MAX(id) FROM company) + 1);
SELECT setval('person_sequence', (SELECT MAX(id) FROM person) + 1);
SELECT setval('team_sequence', (SELECT MAX(id) FROM team) + 1);
