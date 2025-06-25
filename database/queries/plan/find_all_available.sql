-- Active: 1747396923823@@127.0.0.1@3307@netbooks
SELECT plan, name, description, duration FROM plans_editions WHERE closed_in IS NULL;