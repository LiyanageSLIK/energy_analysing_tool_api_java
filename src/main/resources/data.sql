--SUBSCRIPTION_PLANS
INSERT INTO greenbill.subscription_plan
(name, rate, currency_code, cycle, plan_type, max_num_project, max_num_node, status)VALUES
('FREE',0.00, 'LKR','UNLIMITED','FREE',3,10,'ACTIVE');
INSERT INTO greenbill.subscription_plan
(name, rate, currency_code, cycle, plan_type, max_num_project, max_num_node, status) VALUES
('DOMESTIC_LITE',3000.00, 'LKR','SIX_MONTH','PAID',5,25,'ACTIVE');
INSERT INTO greenbill.subscription_plan
(name, rate, currency_code, cycle, plan_type, max_num_project, max_num_node, status) VALUES
('INDUSTRIAL_FREEDOM',80000.00, 'LKR','SIX_MONTH','PAID',50,250,'INACTIVE');
INSERT INTO greenbill.subscription_plan
(name, rate, currency_code, cycle, plan_type, max_num_project, max_num_node, status) VALUES
('ADMIN_PLAN',00.00, 'LKR','UNLIMITED','ADMIN',NULL,NULL,'ACTIVE');

--CEB_TARIFF
--DOMESTIC_UNDER_60KWH
INSERT INTO greenbill.tariff
(batch_id, country, currency_code, category, limited_from, limited_to, lower_limit, upper_limit, energy_charge, fixed_charge, levy) VALUES
('2023-02-15','SRI LANKA','LKR','Domestic',0,60,0,30,30,400,0.025);
INSERT INTO greenbill.tariff
(batch_id, country, currency_code, category, limited_from, limited_to, lower_limit, upper_limit, energy_charge, fixed_charge, levy) VALUES
('2023-02-15','SRI LANKA','LKR','Domestic',0,60,31,60,37,550,0.025);

--DOMESTIC_ABOVE_60KWH
INSERT INTO greenbill.tariff
(batch_id, country, currency_code, category, limited_from, limited_to, lower_limit, upper_limit, energy_charge, fixed_charge, levy) VALUES
('2023-02-15','SRI LANKA','LKR','Domestic',60,2147483647,0,90,42,650,0.025);
INSERT INTO greenbill.tariff
(batch_id, country, currency_code, category, limited_from, limited_to, lower_limit, upper_limit, energy_charge, fixed_charge, levy) VALUES
('2023-02-15','SRI LANKA','LKR','Domestic',60,2147483647,91,180,50,1500,0.025);
INSERT INTO greenbill.tariff
(batch_id, country, currency_code, category, limited_from, limited_to, lower_limit, upper_limit, energy_charge, fixed_charge, levy) VALUES
('2023-02-15','SRI LANKA','LKR','Domestic',60,2147483647,181,2147483647,75,2000,0.025);

--RELIGIOUS & CHARITABLE INSTITUTIONS
INSERT INTO greenbill.tariff
(batch_id, country, currency_code, category, limited_from, limited_to, lower_limit, upper_limit, energy_charge, fixed_charge, levy) VALUES
('2023-02-15','SRI LANKA','LKR','ReligiousAndCharitable',0,2147483647,0,30,30,400,0.025);
INSERT INTO greenbill.tariff
(batch_id, country, currency_code, category, limited_from, limited_to, lower_limit, upper_limit, energy_charge, fixed_charge, levy) VALUES
('2023-02-15','SRI LANKA','LKR','ReligiousAndCharitable',0,2147483647,31,90,37,550,0.025);
INSERT INTO greenbill.tariff
(batch_id, country, currency_code, category, limited_from, limited_to, lower_limit, upper_limit, energy_charge, fixed_charge, levy) VALUES
('2023-02-15','SRI LANKA','LKR','ReligiousAndCharitable',0,2147483647,91,120,42,650,0.025);
INSERT INTO greenbill.tariff
(batch_id, country, currency_code, category, limited_from, limited_to, lower_limit, upper_limit, energy_charge, fixed_charge, levy) VALUES
('2023-02-15','SRI LANKA','LKR','ReligiousAndCharitable',0,2147483647,121,180,45,1500,0.025);
INSERT INTO greenbill.tariff
(batch_id, country, currency_code, category, limited_from, limited_to, lower_limit, upper_limit, energy_charge, fixed_charge, levy) VALUES
('2023-02-15','SRI LANKA','LKR','ReligiousAndCharitable',0,2147483647,181,2147483647,50,2000,0.025);
