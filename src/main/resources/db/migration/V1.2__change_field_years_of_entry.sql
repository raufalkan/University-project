ALTER TABLE groups 
  RENAME COLUMN yearEntry TO yearEntry_old;

ALTER TABLE groups
  ADD yearEntry INT;
  
UPDATE groups
  SET yearEntry = (SELECT EXTRACT (YEAR FROM yearEntry_old))
  WHERE yearEntry IS NULL;
