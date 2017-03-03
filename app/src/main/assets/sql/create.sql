-- Create Notebooks table
CREATE
        TABLE notebooks
        (
                notebook_id INTEGER PRIMARY KEY,
                name TEXT,
                description TEXT,
                color TEXT,
                category_id INTEGER DEFAULT null
        );

-- Create Notes table
CREATE
        TABLE notes
        (
                creation INTEGER PRIMARY KEY,
                last_modification INTEGER,
                title TEXT,
                content TEXT,
                archived INTEGER,
                trashed INTEGER,
                notebook_id INTEGER,
                checklist INTEGER,
                FOREIGN KEY(notebooks) REFERENCES notebooks(notebook_id) ON DELETE CASCADE
        );

-- Create Categories table
CREATE
        TABLE categories
        (
                category_id INTEGER PRIMARY KEY,
                name TEXT,
                description TEXT,
                color TEXT
        );

-- Create Tags table
CREATE
        TABLE tags
        (
                tag_id INTEGER PRIMARY KEY,
                name TEXT,
        )

-- Create Tagmap table
CREATE
        TABLE tagmap
        (
                tagmap_id INTEGER PRIMARY KEY,
                tag_id INTEGER,
                note_id INTEGER
                FOREIGN KEY(tag_id) REFERENCES tags(tag_id)
                FOREIGN KEY(note_id) REFERENCES notes(creation)
        );