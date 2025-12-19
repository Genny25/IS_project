DROP DATABASE IF EXISTS Audire;
CREATE DATABASE Audire;
USE Audire;

CREATE TABLE User (
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    PhoneNumber CHAR(10) NOT NULL,
    Role ENUM('Performer', 'CastingDirector', 'ProductionManager') NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    RegistrationDate DATE NOT NULL
);

CREATE TABLE Performer (
    PerformerID INT PRIMARY KEY AUTO_INCREMENT,
    Gender ENUM('M', 'F', 'Altro'),
    Category ENUM('Attore/Attrice', 'Musicista', 'Cantante', 'Ballerino', 'Doppiatore/trice', 'Qualsiasi') NOT NULL,
    Description TEXT,
    CV_Data MEDIUMBLOB,
    CV_MimeType VARCHAR(255) DEFAULT 'application/pdf',
    ProfilePhoto VARCHAR(255),
    UserID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

CREATE TABLE Production_Manager (
    PmID INT PRIMARY KEY,
    UserID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

CREATE TABLE Casting_Director (
    CdID INT PRIMARY KEY,
    UserID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

CREATE TABLE Production (
    ProductionID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(255) NOT NULL,
    Type ENUM('Serie TV', 'Film', 'Teatro', 'Musical', 'Pubblicità', 'Documentario', 'Cortometraggio', 'Web Series', 'Altro') NOT NULL,
    CreationDate DATE NOT NULL,
    PmID INT NOT NULL,
    FOREIGN KEY (PmID) REFERENCES Production_Manager(PmID) ON DELETE CASCADE
);

CREATE TABLE Team (
    ProductionID INT,
    CdID INT,
    PRIMARY KEY(ProductionID, CdID),
    FOREIGN KEY (ProductionID) REFERENCES Production(ProductionID) ON DELETE CASCADE,
    FOREIGN KEY (CdID) REFERENCES Casting_Director(CdID) ON DELETE CASCADE
);

CREATE TABLE Casting (
    CastingID INT PRIMARY KEY AUTO_INCREMENT,
    Location VARCHAR(255) NOT NULL,
    Category ENUM('Attore/Attrice', 'Musicista', 'Cantante', 'Ballerino', 'Doppiatore/trice', 'Qualsiasi') NOT NULL,
    Description TEXT NOT NULL,
    PublishDate DATE NOT NULL,
    DeadLine DATE NOT NULL,
    Title VARCHAR(255) NOT NULL,
    CdID INT NOT NULL,
    ProductionID INT NOT NULL,
    FOREIGN KEY (CdID) REFERENCES Casting_Director(CdID) ON DELETE CASCADE,
    FOREIGN KEY (ProductionID) REFERENCES Production(ProductionID) ON DELETE CASCADE
);

CREATE TABLE Application (
    ApplicationID INT PRIMARY KEY AUTO_INCREMENT,
    SendingDate DATE NOT NULL,
    Status ENUM('In attesa', 'Shortlist', 'Selezionata', 'Rifiutata') DEFAULT 'In attesa',
    Feedback TEXT,
    PerformerID INT NOT NULL,
    CastingID INT NOT NULL,
    FOREIGN KEY (PerformerID) REFERENCES Performer(PerformerID) ON DELETE CASCADE,
    FOREIGN KEY (CastingID) REFERENCES Casting(CastingID) ON DELETE CASCADE
);