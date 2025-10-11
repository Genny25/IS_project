

CREATE TABLE Users (
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    Role ENUM('Admin', 'CastingDirector', 'ProductionManager', 'Performer') NOT NULL,
    Email VARCHAR(255) UNIQUE NOT NULL
);

CREATE INDEX idx_users_email ON Users(Email);
CREATE INDEX idx_users_role ON Users(Role);
CREATE INDEX idx_castings_deadline ON Castings(DeadLine);

CREATE TABLE Performers (
    PerformerID INT PRIMARY KEY AUTO_INCREMENT,
    PhoneNumber CHAR(10),
    Gender ENUM('M', 'F', 'Other', 'PreferNotToSay'),
    Category VARCHAR(100),
    Description TEXT,
    CV_Data MEDIUMBLOB,  -- fino a 16MB
    CV_MimeType VARCHAR(50) DEFAULT 'application/pdf',
    FOREIGN KEY (PerformerID) REFERENCES Users(UserID) ON DELETE CASCADE
);

CREATE TABLE Production_Managers (
    PmID INT PRIMARY KEY,
    FOREIGN KEY (PmID) REFERENCES Users(UserID) ON DELETE CASCADE
);

CREATE TABLE Casting_Directors (
    CdID INT PRIMARY KEY,
    FOREIGN KEY (CdID) REFERENCES Users(UserID) ON DELETE CASCADE
);

CREATE TABLE Productions (
    ProductionID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(255) NOT NULL,
    Type VARCHAR(100),
    PmID INT NOT NULL,
    FOREIGN KEY (PmID) REFERENCES Production_Managers(PmID) ON DELETE RESTRICT
);

CREATE INDEX idx_productions_pmid ON Productions(PmID);

CREATE TABLE Castings (
    CastingID INT PRIMARY KEY AUTO_INCREMENT,
    Location VARCHAR(255),
    Category VARCHAR(100),
    Description TEXT,
    Date DATE,
    DeadLine DATE,
    Title VARCHAR(255) NOT NULL,
    CdID INT NOT NULL,
    ProductionID INT NOT NULL,
    FOREIGN KEY (CdID) REFERENCES Casting_Directors(CdID) ON DELETE RESTRICT,
    FOREIGN KEY (ProductionID) REFERENCES Productions(ProductionID) On DELETE RESTRICT
);

CREATE INDEX idx_castings_date ON Castings(Date);
CREATE INDEX idx_castings_cdid ON Castings(CdID);

CREATE TABLE Apply (
    PerformerID INT,
    CastingID INT,
    Feedback TEXT,
    Status ENUM('In Attesa', 'ShortList', 'Selezionata', 'Rifiutata') DEFAULT 'In Attesa',
    ApplicationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (PerformerID, CastingID),
    FOREIGN KEY (PerformerID) REFERENCES Performers(PerformerID) ON DELETE CASCADE,
    FOREIGN KEY (CastingID) REFERENCES Castings(CastingID) ON DELETE CASCADE
);

CREATE INDEX idx_apply_status ON Apply(Status);


